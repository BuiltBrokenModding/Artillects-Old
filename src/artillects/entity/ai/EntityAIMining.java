package artillects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.InventoryHelper;
import artillects.Vector3;
import artillects.entity.EntityArtillectGround;
import artillects.entity.workers.EntityWorker;
import artillects.hive.ArtillectType;
import artillects.hive.zone.ZoneMining;

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
		return this.entity.getType() == ArtillectType.HARVESTER;
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
		if (entity.getZone() instanceof ZoneMining && ((ZoneMining) entity.getZone()).scannedBlocks.size() > 0 && !this.entity.isInventoryFull())
		{

			Vector3 targetPosition = null;

			/** Find closest resource block to mine for. */
			for (Vector3 checkVec : ((ZoneMining) entity.getZone()).scannedBlocks)
			{
				if (targetPosition == null || checkVec.distance(new Vector3(this.entity)) < targetPosition.distance(new Vector3(this.entity)))
				{
					targetPosition = checkVec;
				}
			}
			// checks if the entity is within range before setting the path
			if (new Vector3(this.entity).distance(targetPosition.clone().add(0.5)) > 2)
			{
				if (this.lastMoveTime-- <= 0)
				{
					this.entity.tryToWalkNextTo(targetPosition, this.moveSpeed);
					this.lastMoveTime = 40;
				}
			}
			else
			{
				this.entity.setPathToEntity(null);
			}

			MovingObjectPosition mop = this.world.rayTraceBlocks_do_do(Vec3.createVectorHelper(this.entity.posX, this.entity.posY, this.entity.posZ), targetPosition.clone().add(0.5).toVec3(), false, false);

			if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
			{
				Vector3 breakPosition = new Vector3(mop.blockX, mop.blockY, mop.blockZ);
				Vector3 centerVector = breakPosition.clone().add(0.5);

				this.entity.getLookHelper().setLookPosition(centerVector.x, centerVector.y, centerVector.z, 10, 0);

				int blockID = this.world.getBlockId((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z);

				if (blockID != 0)
				{
					this.breakingTime++;

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

						if (this.breakingTime % 10 == 0)
						{
							Artillects.proxy.renderLaser(this.world, new Vector3(this.entity).add(0, 0.2, 0), centerVector, 1, 0, 0);
						}
					}
				}
			}
		}
		else
		{
			// TODO: Need optimal chest!
			Vector3 optimalChestPosition = null;
			TileEntityChest optimalChest = null;

			if (optimalChest != null && !InventoryHelper.isInventoryFull(optimalChest))
			{
				for (int i = 0; i < this.entity.inventory.getSizeInventory(); i++)
				{
					this.entity.tryToWalkNextTo(optimalChestPosition, this.moveSpeed);

					if (optimalChestPosition.distance(new Vector3(this.entity)) < EntityArtillectGround.interactionDistance)
					{
						this.entity.getNavigator().clearPathEntity();
						this.entity.inventory.setInventorySlotContents(i, InventoryHelper.addStackToInventory(optimalChest, this.entity.inventory.getStackInSlot(i)));
					}

					break;
				}
			}
		}
	}
}
