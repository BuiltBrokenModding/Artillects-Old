package artillects.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
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
			if (takeOperation)
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
								if (itemStack.isItemEqual(new ItemStack(Item.coal)))
								{
									if (this.entity.getNavigator().tryMoveToXYZ(chestPosition.x, chestPosition.y, chestPosition.z, this.moveSpeed))
									{
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
