package artillects.drone.entity.ai.work;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import resonant.lib.type.Pair;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.IVectorWorld;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.workers.EntityFabricator;
import artillects.drone.hive.zone.ZoneBuilding;

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
        if (this.entity.getZone() instanceof ZoneBuilding && !((ZoneBuilding) entity.getZone()).buildPosition.isEmpty())
        {
            return true;
        }

        return false;
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

        if (this.idleTime <= 0 && this.entity.getZone() instanceof ZoneBuilding && !((ZoneBuilding) entity.getZone()).buildPosition.isEmpty())
        {
            if (this.placementSpot == null)
            {
                Pair<Vector3, ItemStack> data = ((ZoneBuilding) this.entity.getZone()).getClosestBlock(new VectorWorld((IVectorWorld)this.entity));
                if (data != null && data.left() != null && data.right() != null)
                {
                    this.placementItem = data.right();
                    this.placementSpot = data.left();
                }
            }

            if (this.placementSpot != null)
            {
                if (placementSpot.distance(new Vector3((IVector3)this.entity)) > EntityArtillectGround.interactionDistance)
                {
                    if (!this.entity.tryToWalkNextTo(placementSpot, this.moveSpeed))
                    {
                        this.placementSpot = null;
                    }
                }
                else
                {
                    this.entity.getNavigator().clearPathEntity();
                    ((ZoneBuilding) this.entity.getZone()).placeBlock(placementSpot, placementItem);
                }
            }
            this.idleTime = 20;
        }
    }
}
