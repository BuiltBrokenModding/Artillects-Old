package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import com.builtbroken.artillects.core.entity.ai.AITask;
import com.builtbroken.mc.imp.transform.vector.Pos;
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

    private Pos targetPosition;

    /** Delay to peg on nav timer each time we fail to pathfind to target */
    private int failedPathFindingPenalty;

    /** Did we have a target previous tick */
    private boolean hadTarget = false;

    /** Should we ensure we have a weapon before closing distance */
    protected boolean checkWeapon = true;


    public AITaskMoveTowardsTarget(EntityArtillect entity, double attackMoveSpeed)
    {
        super(entity);
        this.speedTowardsTarget = attackMoveSpeed;
    }

    public AITaskMoveTowardsTarget(EntityArtillect entity, double attackMoveSpeed, boolean checkWeapon)
    {
        this(entity, attackMoveSpeed);
        this.checkWeapon = checkWeapon;
    }

    @Override
    public void updateTask()
    {
        EntityLivingBase target = this.host.getAttackTarget();

        //Cleanup if target is null or dead
        if (target == null || !target.isEntityAlive())
        {
            hadTarget = false;
            targetPosition = null;
            if (target != null)
            {
                entity().setAttackTarget(null);
            }
            return;
        }

        //If we need to check weapons, and are not ignore the check, and don't have a weapon then we do not chase target
        if(checkWeapon && !entity().ignoreWeaponCheck && !entity().hasWeapon())
        {
            return;
        }

        //Init if target is not null and previously we had no target
        if (!hadTarget)
        {
            hadTarget = true;
            entityPathEntity = this.host.getNavigator().getPathToEntityLiving(target);
            this.host.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
            this.navTimer = 0;
        }

        double distanceSq = this.host.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ);
        this.host.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F); //TODO ensure we are looking at target before attacking

        --this.navTimer;

        //TODO add can see code, move this to a separate method as it only updates navTimer
        //TODO move to path finding code, as this is mostly path finder driven
        //TODO add guessing to pathfinder
        if (this.navTimer <= 0 && (targetPosition == null || targetPosition.distance(target) >= 0.5D || this.host.getRNG().nextFloat() < 0.05F)) //TODO why the random
        {
            this.targetPosition = new Pos(target.posX, target.boundingBox.minY, target.posZ);
            this.navTimer = failedPathFindingPenalty + 4 + this.host.getRNG().nextInt(7);

            if (this.host.getNavigator().getPath() != null)
            {
                //If path is to far from target, we failed to path?
                PathPoint finalPathPoint = this.host.getNavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && target.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
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

            //Increase nav timer by distance(in 16 block segments) by 5 ticks
            this.navTimer += (distanceSq / 256) * 5;

            //Update path to target, if path is not set increase timer
            if (!this.host.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget))
            {
                this.navTimer += 15;
            }
        }
    }
}