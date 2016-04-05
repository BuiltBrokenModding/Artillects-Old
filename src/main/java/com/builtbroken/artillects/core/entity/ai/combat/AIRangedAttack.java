package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class AIRangedAttack extends EntityAIBase
{
    /** The entity the AI instance has been applied to */
    private final EntityArtillect host;

    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set
     * back to the maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int targetTimeLost;
    private int minRangedAttackTime;

    /** The maximum time the AI has to wait before performing another ranged attack. */
    private int maxRangedAttackTime;
    private float attackRange;
    private float followDistance;

    public AIRangedAttack(EntityArtillect artillect, double moveSpeed, int minAttackTime, int maxAttackTime, float attackRange)
    {
        this.rangedAttackTime = -1;
        this.host = artillect;
        this.entityMoveSpeed = moveSpeed;
        this.minRangedAttackTime = minAttackTime;
        this.maxRangedAttackTime = maxAttackTime;
        this.attackRange = attackRange;
        this.followDistance = attackRange * attackRange;
        this.setMutexBits(3);
    }

    /** Returns whether the EntityAIBase should begin execution. */
    @Override
    public boolean shouldExecute()
    {
        return host.getAttackTarget() != null;
    }

    /** Returns whether an in-progress EntityAIBase should continue executing */
    @Override
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.host.getNavigator().noPath();
    }

    /** Resets the task */
    @Override
    public void resetTask()
    {
        this.targetTimeLost = 0;
        this.rangedAttackTime = -1;
    }

    /** Updates the task */
    @Override
    public void updateTask()
    {
        double distanceFromTarget = this.host.getDistanceSq(host.getAttackTarget().posX, host.getAttackTarget().boundingBox.minY, host.getAttackTarget().posZ);
        boolean canSeeTarget = true; //this.host.getEntitySenses().canSee(this.attackTarget);

        if (canSeeTarget)
        {
            ++this.targetTimeLost;
        }
        else
        {
            this.targetTimeLost = 0;
        }

        if (distanceFromTarget <= (double) this.followDistance && this.targetTimeLost >= 20)
        {
            this.host.getNavigator().clearPathEntity();
        }
        else
        {
            this.host.getNavigator().tryMoveToEntityLiving(host.getAttackTarget(), this.entityMoveSpeed);
        }

        this.host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0)
        {
            if (distanceFromTarget > (double) this.followDistance || !canSeeTarget)
            {
                return;
            }

            f = MathHelper.sqrt_double(distanceFromTarget) / this.attackRange;

            // TODO replace this.rangedAttackEntityHost.attackEntityWithRangedAttack(thost.getAttackTarget(), f);
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = MathHelper.sqrt_double(distanceFromTarget) / this.attackRange;
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
        }
    }
}
