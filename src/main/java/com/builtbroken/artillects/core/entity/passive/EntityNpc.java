package com.builtbroken.artillects.core.entity.passive;

import com.builtbroken.artillects.core.entity.EntityHumanoid;
import com.builtbroken.artillects.core.entity.ai.AITaskSwimming;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskFindTarget;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMeleeAttack;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMoveTowardsTarget;
import com.builtbroken.artillects.core.entity.profession.Profession;
import com.builtbroken.artillects.core.entity.profession.ProfessionProvider;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntityLivingSelector;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Prefab for all NPC that artillects will contain including crafters and fighters
 *
 * @author Darkguardsman
 */
public class EntityNpc extends EntityHumanoid<InventoryNPC> implements INpc, IEntityAdditionalSpawnData, IPacketIDReceiver
{
    /** Map of profession ids to profession creation objects, for internal use only... will be moved to a handler later */
    public static HashMap<String, ProfessionProvider> registeredProfessions = new HashMap();

    /** Object that handles finer details of what the NPC is, and does */
    public Profession profession;
    /** ID to load NPC's profession from disk, also may be used for render and other references */
    protected String professionID = "citizen"; //Default profession with zero tasks

    /** Location of this NPCs home, defaults to spawn position. Is only used as a back up to an action home object point. */
    private Location homeLocation;

    public EntityNpc(World world)
    {
        super(world);
        inventory = new InventoryNPC(this);

        //AI tasks
        this.tasks.add(0, new AITaskSwimming(this));
        this.tasks.add(2, new AITaskFindTarget(this, new EntityLivingSelector().selectMobs()));
        this.tasks.add(3, new AITaskMoveTowardsTarget(this, 1.0f));
        this.tasks.add(4, new AITaskMeleeAttack(this));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setString("professionID", professionID);
        if (profession != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            profession.save(tag);
            nbt.setTag("professionSave", tag);
        }
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("professionID"))
        {
            this.professionID = nbt.getString("professionID");
        }
        if (professionID != null && registeredProfessions.get(professionID) != null)
        {
            if (nbt.hasKey("professionSave"))
            {
                profession = registeredProfessions.get(professionID).loadFromSave(this, nbt.getCompoundTag("professionSave"));
            }
            else
            {
                profession = registeredProfessions.get(professionID).newProfession(this);
            }
        }
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

    /** Location of this NPCs home, defaults to spawn position */
    public Location getHomeLocation()
    {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation)
    {
        this.homeLocation = homeLocation;
    }

    public float getBlockPathWeight(Pos pos)
    {
        return 0.0F;
    }
}
