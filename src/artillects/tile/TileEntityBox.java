package artillects.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBox extends TileEntity implements IInventory
{
    private ItemStack[] inventoryContents;

    /** Returns the stack in slot i */
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.inventoryContents[par1];
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.inventoryContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventoryContents.length)
            {
                this.inventoryContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    /** Writes a tile entity to NBT. */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventoryContents.length; ++i)
        {
            if (this.inventoryContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventoryContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    /** Removes from an inventory slot (first arg) up to a specified number (second arg) of items and
     * returns them in a new stack. */
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inventoryContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.inventoryContents[par1].stackSize <= par2)
            {
                itemstack = this.inventoryContents[par1];
                this.inventoryContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.inventoryContents[par1].splitStack(par2);

                if (this.inventoryContents[par1].stackSize == 0)
                {
                    this.inventoryContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /** When some containers are closed they call this on each slot, then drop whatever it returns as
     * an EntityItem - like when you close a workbench GUI. */
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inventoryContents[par1] != null)
        {
            ItemStack itemstack = this.inventoryContents[par1];
            this.inventoryContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /** Sets the given item stack to the specified slot in the inventory (can be crafting or armor
     * sections). */
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inventoryContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /** Returns the number of slots in the inventory. */
    @Override
    public int getSizeInventory()
    {
        return 27;
    }

    /** Returns the name of the inventory. */
    @Override
    public String getInvName()
    {
        return "InvBox";
    }

    /** Do not make give this method the name canInteractWith because it clashes with Container */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    /** Returns true if automation is allowed to insert the given stack (ignoring stack size) into
     * the given slot. */
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

}
