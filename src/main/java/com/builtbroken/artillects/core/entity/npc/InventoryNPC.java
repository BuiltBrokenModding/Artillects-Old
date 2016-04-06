package com.builtbroken.artillects.core.entity.npc;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketEntity;
import com.builtbroken.mc.prefab.inventory.BasicInventory;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Basic inventory used by all NPCs. Contains a 5 slot gear inventory, and a 10 slot storage inventory.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/2/2016.
 */
public class InventoryNPC extends BasicInventory
{
    /** Main gear, Slot 0 is held item. Slot 1-4 is armor. */
    public final ItemStack[] gear = new ItemStack[5];

    protected EntityNpc npc;

    public InventoryNPC(EntityNpc npc)
    {
        super(15); //TODO gear link added, update if gear size changes
        this.npc = npc;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot >= 0)
        {
            if (slot < gear.length)
            {
                return gear[slot];
            }
            return super.getStackInSlot(slot);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack insertStack)
    {
        if (slot >= 0)
        {
            if (slot < gear.length)
            {
                ItemStack pre_stack = gear[slot];
                gear[slot] = insertStack;
                if (!InventoryUtility.stacksMatchExact(pre_stack, gear[slot]))
                {
                    markDirty();
                }
            }
            super.setInventorySlotContents(slot, insertStack);
        }
        else
        {
            Engine.error("InventoryNPC: something tried to set " + insertStack + " into slot " + slot + " which is bellow zero.");
        }
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        super.load(nbt);
        if (nbt.hasKey("gear"))
        {
            NBTTagCompound gearTag = nbt.getCompoundTag("gear");
            for (int i = 0; i < gear.length; i++)
            {
                gear[i] = null;
                if (gearTag.hasKey("" + i))
                {
                    gear[i] = ItemStack.loadItemStackFromNBT(gearTag.getCompoundTag("" + i));
                }
            }
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        super.save(nbt);
        NBTTagCompound gearTag = new NBTTagCompound();
        for (int i = 0; i < gear.length; i++)
        {
            if (gear[i] != null && gear[i].getItem() != null && gear[i].stackSize > 0)
            {
                gearTag.setTag("" + i, gear[i].writeToNBT(new NBTTagCompound()));
            }
        }
        if (!gearTag.hasNoTags())
        {
            nbt.setTag("gear", gearTag);
        }
        return nbt;
    }

    @Override
    public void markDirty()
    {
        if(!npc.world().isRemote)
        {
            PacketEntity packet = new PacketEntity(npc, 1);
            npc.writeGearData(packet.data());
            Engine.instance.packetHandler.sendToAllInDimension(packet, npc.worldObj);
        }
    }

    /**
     * Gets the current held item by the NPC
     *
     * @return item
     */
    public ItemStack getHeldItem()
    {
        return gear[0];
    }

    /**
     * Sets the current held item
     *
     * @param stack - item to set
     * @return previous item in the slot
     */
    public ItemStack setHeldItem(ItemStack stack)
    {
        ItemStack pre = gear[0];
        gear[0] = stack;
        return pre;
    }

    /**
     * Gets the helmet the NPC is wearing
     *
     * @return item
     */
    public ItemStack getHelmArmor()
    {
        return gear[4];
    }

    /**
     * Sets the current helm item
     *
     * @param stack - item to set
     * @return previous item in the slot
     */
    public ItemStack setHelmArmor(ItemStack stack)
    {
        ItemStack pre = gear[4];
        gear[4] = stack;
        return pre;
    }

    /**
     * Gets the chest armor the NPC is wearing
     *
     * @return item
     */
    public ItemStack getChestArmor()
    {
        return gear[3];
    }

    /**
     * Sets the current chest armor item
     *
     * @param stack - item to set
     * @return previous item in the slot
     */
    public ItemStack setChestArmor(ItemStack stack)
    {
        ItemStack pre = gear[3];
        gear[3] = stack;
        return pre;
    }

    /**
     * Gets the leg armor the NPC is wearing
     *
     * @return item
     */
    public ItemStack getLegArmor()
    {
        return gear[2];
    }

    /**
     * Sets the current leg armor item
     *
     * @param stack - item to set
     * @return previous item in the slot
     */
    public ItemStack setLegArmor(ItemStack stack)
    {
        ItemStack pre = gear[2];
        gear[2] = stack;
        return pre;
    }

    /**
     * Gets the feet armor the NPC is wearing
     *
     * @return item
     */
    public ItemStack getFootArmor()
    {
        return gear[1];
    }

    /**
     * Sets the current foot armor item
     *
     * @param stack - item to set
     * @return previous item in the slot
     */
    public ItemStack setFootArmor(ItemStack stack)
    {
        ItemStack pre = gear[1];
        gear[1] = stack;
        return pre;
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack stack : gear)
        {
            if(stack != null)
            {
                return false;
            }
        }
        return super.isEmpty();
    }
}
