package artillects.entity.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
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
	private int interactionDistance = 2;

	private final HashSet<ItemStack> stacksToSmelt = new HashSet<ItemStack>();
	private final HashSet<ItemStack> stacksForFuel = new HashSet<ItemStack>();
	private final HashSet<ItemStack> stacksToReturn = new HashSet<ItemStack>();

	public EntityAIBlacksmith(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
		this.setMutexBits(4);

		stacksForFuel.add(new ItemStack(Item.coal));

		stacksToSmelt.add(new ItemStack(Block.oreIron));
		stacksToSmelt.add(new ItemStack(Block.oreGold));

		stacksToReturn.add(new ItemStack(Item.ingotIron));
		stacksToReturn.add(new ItemStack(Item.ingotGold));
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

			if (this.entity.isInventoryEmpty())
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
		boolean containsInput = listContainsStack(this.stacksToSmelt, this.entity.getInventoryAsList());
		boolean containsFuel = listContainsStack(this.stacksForFuel, this.entity.getInventoryAsList());

		if (containsInput || containsFuel)
		{
			for (Vector3 furnacePosition : ((ZoneProcessing) entity.zone).furnacePositions)
			{
				boolean didDump = false;
				TileEntity tileEntity = this.world.getBlockTileEntity((int) furnacePosition.x, (int) furnacePosition.y, (int) furnacePosition.z);

				if (tileEntity instanceof TileEntityFurnace)
				{
					TileEntityFurnace furnace = ((TileEntityFurnace) tileEntity);

					if (containsInput)
					{
						didDump = this.placeIntoSlot(furnace, furnacePosition, this.stacksToSmelt, 0);
					}

					if (containsFuel)
					{
						didDump = this.placeIntoSlot(furnace, furnacePosition, this.stacksForFuel, 1);
					}

					ItemStack outputSlot = furnace.getStackInSlot(2);

					if (outputSlot != null)
					{
						furnace.setInventorySlotContents(2, this.entity.increaseStackSize(outputSlot));
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
						if (this.listContainsStack(this.stacksToSmelt, itemStack) || this.listContainsStack(this.stacksForFuel, itemStack))
						{
							if (this.entity.tryToWalkNextTo(chestPosition, this.moveSpeed))
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

	private boolean placeIntoSlot(IInventory inventory, Vector3 position, Set checkStacks, int slotID)
	{
		ItemStack tentativeStack = inventory.getStackInSlot(slotID);

		if (tentativeStack == null || (listContainsStack(checkStacks, tentativeStack) && tentativeStack.stackSize < tentativeStack.getMaxStackSize()))
		{
			if (this.entity.tryToWalkNextTo(position, this.moveSpeed))
			{
				if (position.distance(new Vector3(this.entity)) <= this.interactionDistance)
				{
					ItemStack stackToSet = getListContainsStack(checkStacks, this.entity.getInventoryAsList());
					this.entity.decreaseStackSize(stackToSet);

					if (tentativeStack != null && tentativeStack.isItemEqual(stackToSet))
					{
						stackToSet.stackSize += tentativeStack.stackSize;
					}

					inventory.setInventorySlotContents(slotID, stackToSet);
					return true;
				}
			}
		}

		return false;
	}

	private boolean listContainsStack(Set<ItemStack> compareStacks, ItemStack stack)
	{
		return getListContainsStack(compareStacks, stack) != null;
	}

	private ItemStack getListContainsStack(Set<ItemStack> compareStacks, ItemStack stack)
	{
		for (ItemStack checkStack : compareStacks)
		{
			if (stack.isItemEqual(stack))
			{
				return stack;
			}
		}
		return null;
	}

	private boolean listContainsStack(Set<ItemStack> compareStacks, List<ItemStack> checkStacks)
	{
		return this.getListContainsStack(compareStacks, checkStacks) != null;
	}

	private ItemStack getListContainsStack(Set<ItemStack> compareStacks, List<ItemStack> checkStacks)
	{
		for (ItemStack compareStack : compareStacks)
		{
			for (ItemStack check : checkStacks)
			{
				if (check.isItemEqual(compareStack))
				{
					return check;
				}
			}
		}
		return null;
	}
}
