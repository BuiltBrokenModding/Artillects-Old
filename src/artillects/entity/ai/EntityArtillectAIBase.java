package artillects.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.EntityArtillectBase;
import artillects.hive.zone.ZoneProcessing;

/**
 * @author Calclavia
 * 
 */
public abstract class EntityArtillectAIBase extends EntityAIBase
{
	protected World world;
	protected double moveSpeed;

	/** The last chest used by the Artillect. Saved to be "closed" next tick. */
	protected TileEntityChest lastUseChest;

	public EntityArtillectAIBase(World world, double moveSpeed)
	{
		this.world = world;
		this.moveSpeed = moveSpeed;
	}

	protected boolean dumpInventoryToChest()
	{
		if (this.getArtillect().zone instanceof ZoneProcessing)
		{
			for (Vector3 chestPosition : ((ZoneProcessing) getArtillect().zone).chestPositions)
			{
				TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

				if (tileEntity instanceof TileEntityChest)
				{
					TileEntityChest chest = ((TileEntityChest) tileEntity);

					for (int i = 0; i < chest.getSizeInventory(); i++)
					{
						ItemStack itemStack = chest.getStackInSlot(i);

						for (int j = 0; j < this.getArtillect().inventory.getSizeInventory(); j++)
						{
							ItemStack stackInEntity = this.getArtillect().inventory.getStackInSlot(j);

							if (stackInEntity != null)
							{
								if (itemStack == null)
								{
									if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
									{
										if (new Vector3(this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectBase.interactionDistance)
										{
											this.getArtillect().getNavigator().clearPathEntity();
											chest.setInventorySlotContents(i, stackInEntity);
											this.getArtillect().inventory.setInventorySlotContents(j, null);
										}
									}
								}
								else if (itemStack.isItemEqual(stackInEntity) && itemStack.stackSize < itemStack.getMaxStackSize())
								{
									if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
									{
										if (new Vector3(this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectBase.interactionDistance)
										{
											int originalStackSize = itemStack.stackSize;
											itemStack.stackSize = Math.min(itemStack.stackSize + stackInEntity.stackSize, itemStack.getMaxStackSize());
											stackInEntity.stackSize -= itemStack.stackSize - originalStackSize;

											if (stackInEntity.stackSize <= 0)
											{
												this.getArtillect().inventory.setInventorySlotContents(j, null);
											}
										}
									}
								}
								chest.openChest();
								this.lastUseChest = chest;
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	public abstract EntityArtillectBase getArtillect();
}
