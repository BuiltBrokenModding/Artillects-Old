package artillects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.Vector3;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ZoneMining;

public class EntityWorker extends EntityArtillectBase
{
	public enum EnumWorkerType
	{
		HARVESTER;
	}

	public InventoryBasic inventory = new InventoryBasic("gui.worker", false, 9);

	public static final int DATA_TYPE_ID = 12;

	public boolean init = false;

	public EntityWorker(World par1World)
	{
		super(par1World);
		this.tasks.addTask(0, new EntityAIMining(this, 1));
		this.tasks.addTask(1, new EntityAIWander(this, 0.5f));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(DATA_TYPE_ID, (byte) EnumWorkerType.HARVESTER.ordinal());
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		if (!init)
		{
			init = true;
			this.setZone(new ZoneMining(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
		}
	}

	/** @return True if the Worker's inventory is full. (See EntityAIMining) */
	public boolean isInventoryFull()
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack != null)
			{
				if (itemStack.stackSize < 64)
				{
					return false;
				}

				continue;
			}

			return false;
		}

		return true;
	}

	/** @param stack */
	public void increaseStackSize(ItemStack stack)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack == null)
			{
				this.inventory.setInventorySlotContents(i, stack);
				return;
			}
			else if (itemStack.isItemEqual(stack))
			{
				itemStack.stackSize = Math.min(itemStack.stackSize + stack.stackSize, itemStack.getMaxStackSize());
				return;
			}
		}
	}

	@Override
	public boolean interact(EntityPlayer entityPlayer)
	{
		entityPlayer.openGui(Artillects.INSTANCE, GuiIDs.WORKER.ordinal(), this.worldObj, this.entityId, 0, 0);
		return true;
	}

	@Override
	protected void dropEquipment(boolean par1, int par2)
	{
		super.dropEquipment(par1, par2);
		this.dropItemsInChest(this.inventory);
	}

	private void dropItemsInChest(IInventory inventory)
	{
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

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 2; i < this.inventory.getSizeInventory(); ++i)
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

		par1NBTTagCompound.setTag("Items", nbttaglist);

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);

		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 2 && j < this.inventory.getSizeInventory())
			{
				this.inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
			}
		}

	}
}
