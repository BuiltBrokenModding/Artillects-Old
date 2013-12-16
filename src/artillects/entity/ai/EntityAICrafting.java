package artillects.entity.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.Vector3;
import artillects.entity.EntityArtillectBase;
import artillects.entity.EntityWorker;
import artillects.hive.ArtillectType;
import artillects.hive.zone.ZoneProcessing;

public class EntityAICrafting extends EntityAIBase
{
	private EntityWorker entity;
	private World world;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private TileEntityChest lastUseChest;
	private int idleTime = 0;
	private final int maxIdleTime = 20 * 5;

	/**
	 * ItemStack: Stack to craft
	 * 
	 * ItemStack[]: Recipe items
	 */
	private final HashMap<ItemStack, ItemStack[]> stacksToCraft = new HashMap<ItemStack, ItemStack[]>();

	public EntityAICrafting(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
	}

	@Override
	public void startExecuting()
	{
		/**
		 * TODO: Prioritize crafting based on what is needed.
		 */
		// Gear
		stacksToCraft.put(new ItemStack(Artillects.itemParts), new ItemStack[] { new ItemStack(Item.ingotIron, 4) });
	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return this.entity.getType() == ArtillectType.CRAFTER && entity.zone instanceof ZoneProcessing;
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
		if (this.lastUseChest != null)
		{
			this.lastUseChest.closeChest();
			this.lastUseChest = null;
		}
		this.idleTime--;

		if (this.idleTime <= 0)
		{
			Iterator<Entry<ItemStack, ItemStack[]>> it = this.stacksToCraft.entrySet().iterator();

			while (it.hasNext())
			{
				Entry<ItemStack, ItemStack[]> entry = it.next();
				ItemStack stackToCraft = entry.getKey();
				ItemStack[] recipeItems = entry.getValue();

				int validItems = 0;

				for (ItemStack recipeItem : recipeItems)
				{
					/**
					 * Check if we have the required resources.
					 */
					int resourceCount = 0;

					for (ItemStack stackInEntity : this.entity.getInventoryAsList())
					{
						if (stackInEntity != null && stackInEntity.isItemEqual(stackToCraft))
						{
							resourceCount += stackInEntity.stackSize;
						}
					}

					/**
					 * If we have enough resources, craft. Otherwise, go find it! If we don't find
					 * the resources, we try to craft the next item on the list.
					 */
					if (resourceCount < stackToCraft.stackSize)
					{
						/**
						 * Search for the resource because we have less than the required amount.
						 */
						if (this.entity.zone instanceof ZoneProcessing)
						{
							ZoneProcessing zone = (ZoneProcessing) this.entity.zone;

							for (Vector3 chestPosition : zone.chestPositions)
							{
								TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

								if (tileEntity instanceof TileEntityChest)
								{
									TileEntityChest chest = (TileEntityChest) tileEntity;

									for (int i = 0; i < chest.getSizeInventory(); i++)
									{
										ItemStack stackInChest = chest.getStackInSlot(i);

										if (stackInChest.isItemEqual(stackToCraft))
										{
											if (this.entity.tryToWalkNextTo(chestPosition, this.moveSpeed))
											{
												if (new Vector3(this.entity).distance(chestPosition.clone().add(0.5)) <= EntityArtillectBase.interactionDistance)
												{
													this.entity.getNavigator().clearPathEntity();
													int resourceToGet = Math.max(stackToCraft.stackSize - resourceCount, 0);
													chest.setInventorySlotContents(i, this.entity.increaseStackSize(stackInChest.splitStack(resourceToGet)));
													return;
												}
											}
										}
									}
								}
							}
						}
					}
					else
					{
						validItems++;
					}
				}

				if (validItems >= recipeItems.length)
				{
					/**
					 * Do Crafting Here.
					 */
					for (ItemStack recipeItem : recipeItems)
					{
						this.entity.decreaseStackSize(recipeItem);
					}

					this.entity.increaseStackSize(stackToCraft);
				}
			}

			this.idleTime = this.maxIdleTime;
		}
	}
}
