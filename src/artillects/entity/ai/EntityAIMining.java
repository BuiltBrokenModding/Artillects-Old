package artillects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.entity.EntityWorker;

public class EntityAIMining extends EntityAIBase
{
	private EntityWorker entity;
	private World world;

	/**
	 * Delay between mining.
	 */
	private int miningDelay;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private int breakingTime;
	private int maxBreakTime = -1;

	public EntityAIMining(EntityWorker entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
		// this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		return true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{

		/**
		 * Check all blocks under this to find ores.
		 */
		int x = (int) this.entity.posX;
		int y = (int) this.entity.posY;
		int z = (int) this.entity.posZ;

		for (; y > 0; y--)
		{
			int blockID = this.world.getBlockId(x, y, z);

			if (blockID == Block.oreIron.blockID)
			{
				return this.entity.isInventoryFull();
			}
		}

		return false;
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.breakingTime = 0;
		/*int breakX = (int) this.entity.posX;
		int breakY = (int) this.entity.posY - 1;
		int breakZ = (int) this.entity.posZ;

		int blockID = this.world.getBlockId(breakX, breakY, breakZ);

		this.world.destroyBlockInWorldPartially(this.entity.entityId, breakX, breakY, breakZ, -1);*/
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		++this.breakingTime;
		int i = (int) ((float) this.breakingTime / 240.0F * 10.0F);

		int breakX = (int) this.entity.posX;
		int breakY = (int) this.entity.posY - 1;
		int breakZ = (int) this.entity.posZ;

		int blockID = this.world.getBlockId(breakX, breakY, breakZ);

		if (blockID != 0)
		{

			if (i != this.maxBreakTime)
			{
				this.world.destroyBlockInWorldPartially(this.entity.entityId, breakX, breakY, breakZ, i);
			}

			if (this.breakingTime == 240)
			{
				List<ItemStack> droppedStacks = Block.blocksList[blockID].getBlockDropped(world, breakX, breakY, breakZ, this.world.getBlockMetadata(breakX, breakY, breakZ), 0);

				for (ItemStack stack : droppedStacks)
				{
					this.entity.increaseStackSize(stack);
				}

				this.world.setBlockToAir(breakX, breakY, breakZ);
				this.world.playAuxSFX(1012, breakX, breakY, breakZ, 0);
			}
		}
		// this.entity.getNavigator().tryMoveToXYZ(this.moveSpeed);
	}
}
