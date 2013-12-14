package artillects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.entity.EntityWorker;
import artillects.hive.ZoneMining;

public class EntityAIMining extends EntityAIBase
{
    private EntityWorker entity;
    private World world;

    /** The speed the creature moves at during mining behavior. */
    private double moveSpeed;
    private int breakingTime;

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
            int breakX = (int) ((ZoneMining) entity.zone).scannedBlocks.get(0).x;
            int breakY = (int) ((ZoneMining) entity.zone).scannedBlocks.get(0).y;
            int breakZ = (int) ((ZoneMining) entity.zone).scannedBlocks.get(0).z;

            this.entity.getNavigator().tryMoveToXYZ(breakX, breakY, breakZ, this.moveSpeed);

            this.breakingTime++;

            int blockID = this.world.getBlockId(breakX, breakY, breakZ);

            if (blockID != 0)
            {
                if (this.breakingTime == 45)
                {
                    List<ItemStack> droppedStacks = Block.blocksList[blockID].getBlockDropped(world, breakX, breakY, breakZ, this.world.getBlockMetadata(breakX, breakY, breakZ), 0);

                    for (ItemStack stack : droppedStacks)
                    {
                        this.entity.increaseStackSize(stack);
                    }

                    this.world.setBlockToAir(breakX, breakY, breakZ);
                    this.world.playAuxSFX(1012, breakX, breakY, breakZ, 0);

                    this.resetTask();
                }
            }
        }
    }
}
