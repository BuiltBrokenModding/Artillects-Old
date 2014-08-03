package artillects.drone.entity.ai.work;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.Vector3;
import artillects.core.Artillects;
import artillects.drone.InventoryHelper;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.EnumArtillectType;
import artillects.drone.entity.workers.EntityWorker;
import artillects.drone.hive.zone.ZoneMining;

public class EntityAIMining extends EntityAILaborTask
{
    private int breakingTime;
    private float maxBreakTime = 60;

    private int lastMoveTime = 0;

    public EntityAIMining(EntityWorker entity, double par2)
    {
        super(entity, par2);
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
        return this.getArtillect().getType() == EnumArtillectType.HARVESTER;
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
        if (getArtillect().getZone() instanceof ZoneMining && ((ZoneMining) getArtillect().getZone()).scannedBlocks.size() > 0 && !this.getArtillect().isInventoryFull())
        {

            Vector3 targetPosition = null;

            /** Find closest resource block to mine for. */
            for (Vector3 checkVec : ((ZoneMining) getArtillect().getZone()).scannedBlocks)
            {
                if (targetPosition == null || checkVec.distance(new Vector3((IVector3)this.getArtillect())) < targetPosition.distance(new Vector3((IVector3)this.getArtillect())))
                {
                    targetPosition = checkVec;
                }
            }
            // checks if the entity is within range before setting the path
            if (new Vector3((IVector3)this.getArtillect()).distance(targetPosition.clone().add(0.5)) > 2)
            {
                if (this.lastMoveTime-- <= 0)
                {
                    this.getArtillect().tryToWalkNextTo(targetPosition, this.moveSpeed);
                    this.lastMoveTime = 40;
                }
            }
            else
            {
                this.getArtillect().setPathToEntity(null);
            }

            MovingObjectPosition mop = world().rayTraceBlocks_do_do(Vec3.createVectorHelper(this.getArtillect().posX, this.getArtillect().posY, this.getArtillect().posZ), targetPosition.clone().add(0.5).toVec3(), false, false);

            if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
            {
                Vector3 breakPosition = new Vector3(mop.blockX, mop.blockY, mop.blockZ);
                Vector3 centerVector = breakPosition.clone().add(0.5);

                this.getArtillect().getLookHelper().setLookPosition(centerVector.x, centerVector.y, centerVector.z, 10, 0);

                int blockID = world().getBlockId((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z);

                if (blockID != 0)
                {
                    this.breakingTime++;

                    if (this.breakingTime >= this.maxBreakTime)
                    {
                        List<ItemStack> droppedStacks = Block.blocksList[blockID].getBlockDropped(world(), (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, this.world().getBlockMetadata((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z), 0);

                        for (ItemStack stack : droppedStacks)
                        {
                            this.getArtillect().increaseStackSize(stack);
                        }

                        this.world().setBlockToAir((int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z);
                        this.world().playAuxSFX(1012, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, 0);
                        this.world().destroyBlockInWorldPartially(this.getArtillect().entityId, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, -1);
                        this.resetTask();
                    }
                    else
                    {
                        int i = (int) (this.breakingTime / this.maxBreakTime * 10f);
                        this.world().destroyBlockInWorldPartially(this.getArtillect().entityId, (int) breakPosition.x, (int) breakPosition.y, (int) breakPosition.z, i);

                        if (this.breakingTime % 10 == 0)
                        {
                            Artillects.proxy.renderLaser(this.world(), new Vector3((IVector3)this.getArtillect()).translate(0, 0.2, 0), centerVector, 1, 0, 0);
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
                for (int i = 0; i < this.getArtillect().inventory.getSizeInventory(); i++)
                {
                    this.getArtillect().tryToWalkNextTo(optimalChestPosition, this.moveSpeed);

                    if (optimalChestPosition.distance(new Vector3((IVector3)this.getArtillect())) < EntityArtillectGround.interactionDistance)
                    {
                        this.getArtillect().getNavigator().clearPathEntity();
                        this.getArtillect().inventory.setInventorySlotContents(i, InventoryHelper.addStackToInventory(optimalChest, this.getArtillect().inventory.getStackInSlot(i)));
                    }

                    break;
                }
            }
        }
    }
}
