package artillects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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

	private int lastMoveTime = 0;

	public EntityAIMining(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
		this.setMutexBits(4);
	}

	@Override
	public void startExecuting()
	{

	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return entity.zone instanceof ZoneMining && !((ZoneMining) entity.zone).scannedBlocks.isEmpty() && !this.entity.isInventoryFull();
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
			Vector3 targetPosition = null;

			/**
			 * Find closest resource block to mine for.
			 */
			for (Vector3 checkVec : ((ZoneMining) entity.zone).scannedBlocks)
			{
				if (targetPosition == null || checkVec.distance(new Vector3(this.entity)) < targetPosition.distance(new Vector3(this.entity)))
				{
					targetPosition = checkVec;
				}
			}

			if (this.lastMoveTime-- <= 0)
			{
				this.entity.getNavigator().tryMoveToXYZ(targetPosition.x, targetPosition.y, targetPosition.z, this.moveSpeed);
				this.lastMoveTime = 40;
			}

			MovingObjectPosition mop = this.world.rayTraceBlocks_do_do(Vec3.createVectorHelper(this.entity.posX, this.entity.posY, this.entity.posZ), targetPosition.toVec3(), false, false);

			if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
			{
				Vector3 breakPosition = new Vector3(mop.blockX, mop.blockY, mop.blockZ);

				int blockID = this.world.getBlockId((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z);

				if (blockID != 0)
				{
					this.breakingTime++;
					System.out.println(this.breakingTime);
					if (this.breakingTime >= this.maxBreakTime)
					{
						List<ItemStack> droppedStacks = Block.blocksList[blockID].getBlockDropped(world, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, this.world.getBlockMetadata((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z), 0);

						for (ItemStack stack : droppedStacks)
						{
							this.entity.increaseStackSize(stack);
						}

						this.world.setBlockToAir((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z);
						this.world.playAuxSFX(1012, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, 0);
						this.world.destroyBlockInWorldPartially(this.entity.entityId, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, -1);
						this.resetTask();
					}
					else
					{
						int i = (int) (this.breakingTime / this.maxBreakTime * 10f);

						this.world.destroyBlockInWorldPartially(this.entity.entityId, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, i);
					}

				}
			}
		}
	}
}
