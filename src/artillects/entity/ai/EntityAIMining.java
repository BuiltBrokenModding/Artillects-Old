package artillects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.EntityWorker;
import artillects.hive.ZoneMining;

public class EntityAIMining extends EntityAIBase
{
	private EntityWorker entity;
	private World world;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private int breakingTime;
	private float maxBreakTime = 60;

	public EntityAIMining(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
	}

	@Override
	public void startExecuting()
	{

	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return entity.zone instanceof ZoneMining && !((ZoneMining) entity.zone).scannedSortedPositions.isEmpty() && !this.entity.isInventoryFull();
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
		this.breakingTime = 0;
	}

	/** Updates the task */
	@Override
	public void updateTask()
	{
		if (((ZoneMining) entity.zone).scannedBlocks.size() > 0)
		{
			Vector3 breakBlock = null;

			/**
			 * Find closest resource block to mine for.
			 */
			for (Vector3 checkVec : ((ZoneMining) entity.zone).scannedBlocks)
			{
				if (breakBlock == null || checkVec.distance(new Vector3(this.entity)) < breakBlock.distance(new Vector3(this.entity)))
				{
					breakBlock = checkVec;
				}
			}

			this.entity.getNavigator().tryMoveToXYZ(breakBlock.x, breakBlock.y, breakBlock.z, this.moveSpeed);

			this.breakingTime++;
			System.out.println(breakBlock + " : " + this.breakingTime);

			int blockID = this.world.getBlockId((int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z);

			if (blockID != 0)
			{
				if (this.breakingTime >= this.maxBreakTime)
				{
					List<ItemStack> droppedStacks = Block.blocksList[blockID].getBlockDropped(world, (int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z, this.world.getBlockMetadata((int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z), 0);

					for (ItemStack stack : droppedStacks)
					{
						this.entity.increaseStackSize(stack);
					}

					this.world.setBlockToAir((int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z);
					this.world.playAuxSFX(1012, (int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z, 0);
					this.world.destroyBlockInWorldPartially(this.entity.entityId, (int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z, -1);
					this.resetTask();
				}
				else
				{
					int i = (int) (this.breakingTime / this.maxBreakTime * 10f);
					this.world.destroyBlockInWorldPartially(this.entity.entityId, (int) breakBlock.x, (int) breakBlock.y, (int) breakBlock.z, i);
				}
			}
		}
	}
}
