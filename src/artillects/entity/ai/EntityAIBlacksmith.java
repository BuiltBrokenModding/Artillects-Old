package artillects.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.EntityWorker;
import artillects.entity.EntityWorker.EnumWorkerType;
import artillects.hive.ZoneProcessing;

public class EntityAIBlacksmith extends EntityAIBase
{
	private EntityWorker entity;
	private World world;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private boolean takeOperation;
	private TileEntityChest lastUseChest;
	private int interactionDistance = 3;

	public EntityAIBlacksmith(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
		this.setMutexBits(4);
	}

	@Override
	public void startExecuting()
	{
		this.takeOperation = true;
	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return this.entity.getType() == EnumWorkerType.BLACKSMITH && entity.zone instanceof ZoneProcessing;
	}

	/** Returns whether an in-progress EntityAIBase should continue executing */
	@Override
	public boolean continueExecuting()
	{
		return this.shouldExecute();
	}

	/** Resets the task */
	@Override
	public void resetTask()
	{
	}

	/** Updates the task */
	@Override
	public void updateTask()
	{
		if (((ZoneProcessing) entity.zone).chestPositions.size() > 0 && ((ZoneProcessing) entity.zone).furnacePositions.size() > 0)
		{
			if (this.lastUseChest != null)
			{
				this.lastUseChest.numUsingPlayers--;
			}

			if (!this.entity.isInventoryFull())
			{
				this.takeResources();
			}
			else
			{
				if (!this.dumpToBeProcessed())
				{
					this.dumpProcessed();
				}
			}

			if (this.takeOperation)
			{
			}
			else
			{

				this.resetTask();
			}
		}
	}

	private boolean dumpProcessed()
	{
		/*
		 * for (Vector3 chestPosition : ((ZoneProcessing) entity.zone).chestPositions) { TileEntity
		 * tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y,
		 * (int) chestPosition.z);
		 * 
		 * if (tileEntity instanceof TileEntityChest) { TileEntityChest chest = ((TileEntityChest)
		 * tileEntity);
		 * 
		 * for (int i = 0; i < chest.getSizeInventory(); i++) { ItemStack itemStack =
		 * chest.getStackInSlot(i);
		 * 
		 * if (itemStack != null) { if (itemStack.isItemEqual(new ItemStack(Item.coal))) { if
		 * (this.entity.getNavigator().tryMoveToXYZ(chestPosition.x, chestPosition.y,
		 * chestPosition.z, this.moveSpeed)) { if (chestPosition.distance(new Vector3(this.entity))
		 * <= interactionDistance) { this.entity.increaseStackSize(itemStack.splitStack(1));
		 * 
		 * if (itemStack.stackSize <= 0) { chest.setInventorySlotContents(i, null); }
		 * 
		 * chest.numUsingPlayers++;
		 * 
		 * this.lastUseChest = chest; this.takeOperation = false; }
		 * 
		 * return true; } } } } } }
		 */
		return false;
	}

	private boolean dumpToBeProcessed()
	{
		boolean hasIron = this.entity.hasItem(new ItemStack(Item.ingotIron));
		boolean hasCoal = this.entity.hasItem(new ItemStack(Item.coal));

		if (hasIron || hasCoal)
		{
			for (Vector3 furnacePosition : ((ZoneProcessing) entity.zone).furnacePositions)
			{
				boolean didDump = false;
				TileEntity tileEntity = this.world.getBlockTileEntity((int) furnacePosition.x, (int) furnacePosition.y, (int) furnacePosition.z);

				if (tileEntity instanceof TileEntityFurnace)
				{
					TileEntityFurnace furnace = ((TileEntityFurnace) tileEntity);

					ItemStack topSlot = furnace.getStackInSlot(0);

					if (hasIron && (topSlot == null || (topSlot.isItemEqual(new ItemStack(Item.ingotIron)) && topSlot.stackSize < topSlot.getMaxStackSize())))
					{
						if (this.entity.getNavigator().tryMoveToXYZ(furnacePosition.x, furnacePosition.y, furnacePosition.z, this.moveSpeed))
						{
							if (furnacePosition.distance(new Vector3(this.entity)) <= interactionDistance)
							{
								if (topSlot == null)
								{
									furnace.setInventorySlotContents(0, new ItemStack(Item.ingotIron));
								}

								furnace.getStackInSlot(0).stackSize++;
								this.entity.decreaseStackSize(new ItemStack(Item.ingotIron));
								didDump = true;
							}
						}
					}

					ItemStack bottomSlot = furnace.getStackInSlot(2);

					if (hasCoal && (bottomSlot == null || (bottomSlot.isItemEqual(new ItemStack(Item.coal)) && bottomSlot.stackSize < bottomSlot.getMaxStackSize())))
					{
						if (this.entity.getNavigator().tryMoveToXYZ(furnacePosition.x, furnacePosition.y, furnacePosition.z, this.moveSpeed))
						{
							if (furnacePosition.distance(new Vector3(this.entity)) <= interactionDistance)
							{
								if (bottomSlot == null)
								{
									furnace.setInventorySlotContents(2, new ItemStack(Item.coal));
								}

								furnace.getStackInSlot(2).stackSize++;
								this.entity.decreaseStackSize(new ItemStack(Item.coal));
								didDump = true;
							}
						}
					}

					ItemStack outputSlot = furnace.getStackInSlot(1);

					if (outputSlot != null)
					{
						furnace.setInventorySlotContents(1, this.entity.increaseStackSize(outputSlot));
					}

					return didDump;
				}
			}
		}

		return false;
	}

	private boolean takeResources()
	{
		for (Vector3 chestPosition : ((ZoneProcessing) entity.zone).chestPositions)
		{
			TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

			if (tileEntity instanceof TileEntityChest)
			{
				TileEntityChest chest = ((TileEntityChest) tileEntity);

				for (int i = 0; i < chest.getSizeInventory(); i++)
				{
					ItemStack itemStack = chest.getStackInSlot(i);

					if (itemStack != null)
					{
						if (itemStack.isItemEqual(new ItemStack(Item.coal)) || itemStack.isItemEqual(new ItemStack(Item.ingotIron)))
						{
							if (this.entity.getNavigator().tryMoveToXYZ(chestPosition.x, chestPosition.y, chestPosition.z, this.moveSpeed))
							{
								if (chestPosition.distance(new Vector3(this.entity)) <= interactionDistance)
								{
									this.entity.increaseStackSize(itemStack.splitStack(1));

									if (itemStack.stackSize <= 0)
									{
										chest.setInventorySlotContents(i, null);
									}

									chest.numUsingPlayers++;

									this.lastUseChest = chest;
									this.takeOperation = false;
								}

								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}
