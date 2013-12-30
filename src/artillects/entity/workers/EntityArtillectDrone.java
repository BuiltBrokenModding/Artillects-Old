package artillects.entity.workers;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import artillects.InventoryHelper;
import artillects.VectorWorld;
import artillects.entity.EntityArtillectGround;
import artillects.hive.ArtillectType;
import artillects.hive.HiveComplex;
import artillects.hive.HiveComplexManager;

/** Prefab for non-combat drones that do hard labor for the hive
 * 
 * @author DarkGuardsman */
public class EntityArtillectDrone extends EntityArtillectGround
{

    /** Drone's inventory */
    public InventoryBasic inventory = new InventoryBasic("gui.artillect", false, 9);
    protected List<ItemStack> cachedInventory;

    public EntityArtillectDrone(World world)
    {
        super(world);
    }

    @Override
    public IInventory getInventory()
    {
        return this.inventory;
    }

    /** INVENTORY FUNCTIONS */
    /** @return True if the Worker's inventory is full. (See EntityAIMining) */
    public boolean isInventoryFull()
    {
        return InventoryHelper.isInventoryFull(this.inventory);
    }

    public boolean isInventoryEmpty()
    {
        return InventoryHelper.isInventoryEmpty(this.inventory);
    }

    /** Adds a stack into the inventory.
     * 
     * @param stack - The stack to add
     * @return - The remaining stack. */
    public ItemStack increaseStackSize(ItemStack stack)
    {
        return InventoryHelper.addStackToInventory(this.inventory, stack);
    }

    @Override
    protected void dropEquipment(boolean par1, int par2)
    {
        super.dropEquipment(par1, par2);
        if (inventory != null && !this.worldObj.isRemote)
        {
            for (int i = 0; i < inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inventory.getStackInSlot(i);

                if (itemstack != null)
                {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    public List<ItemStack> getInventoryAsList()
    {
        if (this.cachedInventory == null)
        {
            this.cachedInventory = InventoryHelper.getInventoryAsList(this.inventory);
        }

        return this.cachedInventory;
    }

    public boolean hasItem(ItemStack... itemStacks)
    {
        return InventoryHelper.hasItem(this.inventory, itemStacks);
    }

    public boolean decreaseStackSize(ItemStack itemStack)
    {
        return InventoryHelper.decreaseStackSize(this.inventory, itemStack);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (itemstack != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        NBTTagList nbttaglist = nbt.getTagList("Items");

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j < this.inventory.getSizeInventory())
            {
                this.inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

}
