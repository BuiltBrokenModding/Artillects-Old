package artillects.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.InventoryHelper;
import artillects.Vector3;
import artillects.entity.ai.EntityAIBlacksmith;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ZoneMining;
import artillects.hive.ZoneProcessing;
import artillects.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityWorker extends EntityArtillectBase implements IPacketReceiver
{
	public enum EnumWorkerType
	{
		HARVESTER, BLACKSMITH;
	}

	public InventoryBasic inventory = new InventoryBasic("gui.worker", false, 9);

	private List<ItemStack> cachedInventory;

	public static final int interactionDistance = 2;

	public static final int DATA_TYPE_ID = 12;

	public EntityWorker(World par1World)
	{
		super(par1World);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIMining(this, 1));
		this.tasks.addTask(1, new EntityAIBlacksmith(this, 1));
		this.tasks.addTask(2, new EntityAIWander(this, 0.5f));
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

		if (this.getZone() == null)
		{
			if (this.getType() == EnumWorkerType.HARVESTER)
			{
				this.setZone(new ZoneMining(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
			else
			{
				this.setZone(new ZoneProcessing(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
		}

		this.cachedInventory = null;
	}

	/** @return True if the Worker's inventory is full. (See EntityAIMining) */
	public boolean isInventoryFull()
	{
		return InventoryHelper.isInventoryFull(this.inventory);
	}

	public boolean isInventoryEmpty()
	{
		return InventoryHelper.isInventoryEmpty(this.inventory);
	}

	/**
	 * Adds a stack into the inventory.
	 * 
	 * @param stack - The stack to add
	 * @return - The remaining stack.
	 */
	public ItemStack increaseStackSize(ItemStack stack)
	{
		return InventoryHelper.addStackToInventory(this.inventory, stack);
	}

	public EnumWorkerType getType()
	{
		return EnumWorkerType.values()[this.getDataWatcher().getWatchableObjectByte(EntityWorker.DATA_TYPE_ID)];
	}

	public void setType(EnumWorkerType type)
	{
		if (this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToServer(Artillects.PACKET_ENTITY.getPacket(this, (byte) type.ordinal()));
		}
		else
		{
			this.getDataWatcher().updateObject(EntityWorker.DATA_TYPE_ID, (byte) (type.ordinal()));
		}
	}

	@Override
	public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		// TODO: REMOVE THIS;
		this.setZone(null);
		this.setType(EnumWorkerType.values()[data.readByte()]);
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

		nbt.setByte("type", (byte) this.getType().ordinal());
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

		this.setType(EnumWorkerType.values()[nbt.getByte("type")]);
	}

	public List<ItemStack> getInventoryAsList()
	{
		if (this.cachedInventory == null)
		{
			this.cachedInventory = new ArrayList<ItemStack>();

			for (int i = 0; i < this.inventory.getSizeInventory(); i++)
			{
				this.cachedInventory.add(this.inventory.getStackInSlot(i));
			}
		}

		return this.cachedInventory;
	}

	public boolean hasItem(ItemStack... itemStacks)
	{
		for (int i = 0; i < this.inventory.getSizeInventory(); i++)
		{
			for (ItemStack itemStack : itemStacks)
			{
				if (this.inventory.getStackInSlot(i) != null && itemStack.isItemEqual(this.inventory.getStackInSlot(i)))
				{
					return true;
				}
			}
		}

		return false;
	}

	public boolean decreaseStackSize(ItemStack itemStack)
	{
		for (int i = 0; i < this.inventory.getSizeInventory(); i++)
		{
			if (itemStack.isItemEqual(this.inventory.getStackInSlot(i)))
			{
				this.inventory.decrStackSize(i, itemStack.stackSize);
				return true;
			}
		}

		return false;
	}

	public boolean tryToWalkNextTo(Vector3 position, double moveSpeed)
	{
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			PathEntity path = this.getNavigator().getPathToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ);

			if (path != null)
			{
				this.getNavigator().tryMoveToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ, moveSpeed);
				return true;
			}
		}

		return false;
	}
}
