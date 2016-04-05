package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import com.builtbroken.artillects.core.entity.ai.AITask;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;

/**
 * Simple task for moving towards attack target, doesn't do the actual attacking
 */
public class AITaskMoveTowardsTarget extends AITask<EntityArtillect>
{
    /** The speed with which the mob will approach the target */
    double speedTowardsTarget;
    /** The PathEntity of our entity. */
    PathEntity entityPathEntity;
    /** Timer for rechecking pathfinder */
    private int navTimer;


    private double targetX;
    private double targetY;
    private double targetZ;

    /** Delay to peg on nav timer each time we fail to pathfind to target */
    private int failedPathFindingPenalty;

    /** Did we have a target previous tick */
    private boolean hadTarget = false;


    public AITaskMoveTowardsTarget(EntityArtillect entity, double attackMoveSpeed)
    {
        super(entity);
        this.speedTowardsTarget = attackMoveSpeed;
    }

    public boolean shouldExecute()
    {
        //Update navigation path every X ticks, X is randomized 4 - 11
        if (--this.navTimer <= 0)
        {
            this.entityPathEntity = this.host.getNavigator().getPathToEntityLiving(this.host.getAttackTarget());
            this.navTimer = 4 + this.host.getRNG().nextInt(7);
            return this.entityPathEntity != null;
        }
        return true;
    }


    @Override
    public void updateTask()
    {
        //Cleanup if target is null or dead
        if (entity().getAttackTarget() == null || !entity().getAttackTarget().isEntityAlive())
        {
            hadTarget = false;
            if (entity().getAttackTarget() != null)
            {
                entity().setAttackTarget(null);
            }
            return;
        }

        //Init if target is not null and previously we had no target
        if (entity().getAttackTarget() != null && !hadTarget)
        {
            hadTarget = true;
            this.host.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
            this.navTimer = 0;
        }

        if (shouldExecute())
        {
            EntityLivingBase entitylivingbase = this.host.getAttackTarget();
            this.host.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F); //TODO ensure we are looking at target before attacking
            double distanceSq = this.host.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
            --this.navTimer;

            //TODO add can see code, move this to a separate method as it only updates navTimer
            //TODO move to path finding code, as this is mostly path finder driven
            //TODO add guessing to pathfinder
            if (this.navTimer <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.host.getRNG().nextFloat() < 0.05F))
            {
                this.targetX = entitylivingbase.posX;
                this.targetY = entitylivingbase.boundingBox.minY;
                this.targetZ = entitylivingbase.posZ;
                this.navTimer = failedPathFindingPenalty + 4 + this.host.getRNG().nextInt(7);

                if (this.host.getNavigator().getPath() != null)
                {
                    PathPoint finalPathPoint = this.host.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
                    {
                        failedPathFindingPenalty = 0;
                    }
                    else
                    {
                        failedPathFindingPenalty += 10;
                    }
                }
                else
                {
                    failedPathFindingPenalty += 10;
                }

                if (distanceSq > 1024.0D)
                {
                    this.navTimer += 10;
                }
                else if (distanceSq > 256.0D)
                {
                    this.navTimer += 5;
                }

                if (!this.host.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget))
                {
                    this.navTimer += 15;
                }
            }
        }
    }
}