package artillects.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Pair;
import artillects.Vector3;
import artillects.VectorWorld;
import artillects.entity.EntityArtillectBase;
import artillects.entity.EntityFabricator;
import artillects.hive.zone.ZoneBuilding;

public class EntityAIBuilding extends EntityAIBase
{
    private EntityFabricator entity;
    private World world;

    /** The speed the creature moves at during mining behavior. */
    private double moveSpeed;
    private int idleTime = 0;
    private final int maxIdleTime = 20;
    private Vector3 placementSpot = null;
    private ItemStack placementItem = null;

    public EntityAIBuilding(EntityFabricator entity, double par2)
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
        return this.entity.zone instanceof ZoneBuilding && !((ZoneBuilding) entity.zone).buildPosition.isEmpty();
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
        this.idleTime--;

        if (this.idleTime <= 0 && this.entity.zone instanceof ZoneBuilding && !((ZoneBuilding) entity.zone).buildPosition.isEmpty())
        {
            if (this.placementSpot == null)
            {
                Pair<Vector3, ItemStack> data = ((ZoneBuilding) this.entity.zone).getClosestBlock(new VectorWorld(this.entity));
                if (data != null && data.getLeft() != null && data.getRight() != null)
                {
                    this.placementItem = data.getRight();
                    this.placementSpot = data.getLeft();
                }
            }

            if (this.placementSpot != null)
            {
                if (placementSpot.distance(new Vector3(this.entity)) > EntityArtillectBase.interactionDistance)
                {
                    if (!this.entity.tryToWalkNextTo(placementSpot, this.moveSpeed))
                    {
                        this.placementSpot = null;
                    }
                }
                else
                {
                    this.entity.getNavigator().clearPathEntity();
                    ((ZoneBuilding) this.entity.zone).placeBlock(placementSpot, placementItem);
                }
            }
        }
    }
}
