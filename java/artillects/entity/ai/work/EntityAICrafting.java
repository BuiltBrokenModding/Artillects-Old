package artillects.entity.ai.work;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import universalelectricity.api.vector.Vector3;
import artillects.Artillects;
import artillects.entity.EntityArtillectGround;
import artillects.entity.workers.EntityWorker;
import artillects.hive.EnumArtillectType;
import artillects.hive.zone.ZoneProcessing;
import artillects.item.ItemDroneParts.Part;

public class EntityAICrafting extends EntityAILaborTask
{
	private int idleTime = 0;
	private final int maxIdleTime = 20 * 5;

	/**
	 * ItemStack: Stack to craft
	 * 
	 * ItemStack[]: Recipe items
	 */
	private final HashMap<ItemStack, ItemStack[]> stacksToCraft = new HashMap<ItemStack, ItemStack[]>();
	private boolean markForDump;

	public EntityAICrafting(EntityWorker entity, double moveSpeed)
	{
		super(entity, moveSpeed);
	}

	@Override
	public void startExecuting()
	{
		/**
		 * TODO: Prioritize crafting based on what is needed.
		 */
		// Gear
		stacksToCraft.put(new ItemStack(Artillects.itemParts), new ItemStack[] { new ItemStack(Item.ingotIron, 4) });
		stacksToCraft.put(new ItemStack(Artillects.itemParts, 1, Part.CIRCUITS_T1.ordinal()), new ItemStack[] { new ItemStack(Item.ingotIron, 4), new ItemStack(Item.ingotGold, 4) });

	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return this.getArtillect().getType() == EnumArtillectType.CRAFTER && getArtillect().getZone() instanceof ZoneProcessing;
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
			if (this.markForDump)
			{
				this.markForDump = this.dumpInventoryToChest();
			}
			else
			{
				// TODO: After ModJam, make this crafting modular/work with all items.
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

						for (ItemStack stackInEntity : this.getArtillect().getInventoryAsList())
						{
							if (stackInEntity != null && stackInEntity.isItemEqual(recipeItem))
							{
								resourceCount += stackInEntity.stackSize;
							}
						}

						/**
						 * If we have enough resources, craft. Otherwise, go find it! If we don't
						 * find the resources, we try to craft the next item on the list.
						 */
						if (resourceCount < recipeItem.stackSize)
						{
							/**
							 * Search for the resource because we have less than the required
							 * amount.
							 */
							if (this.getArtillect().getZone() instanceof ZoneProcessing)
							{
								ZoneProcessing zone = (ZoneProcessing) this.getArtillect().getZone();

								for (Vector3 chestPosition : zone.chestPositions)
								{
									TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

									if (tileEntity instanceof TileEntityChest)
									{
										TileEntityChest chest = (TileEntityChest) tileEntity;

										for (int i = 0; i < chest.getSizeInventory(); i++)
										{
											ItemStack stackInChest = chest.getStackInSlot(i);

											if (stackInChest != null && stackInChest.isItemEqual(recipeItem))
											{
												if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
												{
													if (new Vector3(this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectGround.interactionDistance)
													{
														this.getArtillect().getNavigator().clearPathEntity();
														int resourceToGet = Math.max(recipeItem.stackSize - resourceCount, 0);
														ItemStack remainingStack = this.getArtillect().increaseStackSize(stackInChest.splitStack(resourceToGet));
														chest.setInventorySlotContents(i, stackInChest.stackSize > 0 ? stackInChest : null);
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
							this.getArtillect().decreaseStackSize(recipeItem);
						}

						this.getArtillect().increaseStackSize(stackToCraft);
						this.markForDump = true;
					}
				}
			}

			this.idleTime = this.maxIdleTime;
		}
	}
}
