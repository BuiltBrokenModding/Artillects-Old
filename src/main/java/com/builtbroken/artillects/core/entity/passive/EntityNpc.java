package com.builtbroken.artillects.core.entity.passive;

import com.builtbroken.artillects.core.entity.EntityHumanoid;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketType;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Prefab for all NPC that artillects will contain including crafters and fighters
 *
 * @author Darkguardsman
 */
public class EntityNpc extends EntityHumanoid<InventoryNPC> implements INpc, IEntityAdditionalSpawnData, IPacketIDReceiver
{
    public EntityNpc(World world)
    {
        super(world);
        inventory = new InventoryNPC(this);
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack)
    {
        if (slot >= 0 && slot < 5)
        {
            if (inventory != null)
            {
                inventory.setInventorySlotContents(slot, stack);
            }
            else
            {
                Engine.error("EntityNPC: inventory is null so can't set " + stack + " into slot " + slot + "\nEntity = '" + this + "'");
            }
        }
        else
        {
            Engine.error("EntityNPC: something tried to set " + stack + " into slot " + slot + " which is outside the 0 - 5 limit for gear. \nEntity = '" + this + "'");
        }
    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return inventory != null ? inventory.gear : null;
    }

    @Override
    public ItemStack getHeldItem()
    {
        return inventory != null ? inventory.getHeldItem() : null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot)
    {
        return inventory != null && slot >= 0 && slot < 5 ? inventory.gear[slot] : null;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(inventory != null);
        if (inventory != null)
        {
            writeGearData(buffer);
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        //Prevent reading server side
        if (worldObj.isRemote)
        {
            if (additionalData.readBoolean())
            {
                readGearData(additionalData);
            }
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType packet)
    {
        if (worldObj.isRemote)
        {
            if (id == 1)
            {
                readGearData(buf);
                return true;
            }
        }
        return false;
    }

    protected void readGearData(ByteBuf buf)
    {
        for (int i = 0; i < 5; i++)
        {
            inventory.gear[i] = null;
            NBTTagCompound tag = ByteBufUtils.readTag(buf);
            if (tag != null && !tag.hasNoTags())
            {
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                if (stack != null)
                {
                    inventory.gear[i] = stack;
                }
            }
        }
    }

    protected void writeGearData(ByteBuf buf)
    {
        for (ItemStack stack : inventory.gear)
        {
            if (stack != null)
            {
                ByteBufUtils.writeTag(buf, stack.writeToNBT(new NBTTagCompound()));
            }
            else
            {
                ByteBufUtils.writeTag(buf, new NBTTagCompound());
            }
        }
    }
}
