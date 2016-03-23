package com.builtbroken.artillects.core.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class AIRangedAttack extends EntityAIBase
{
    /** The entity the AI instance has been applied to */
    private final EntityLiving entityHost;

    /** The entity (as a RangedAttackMob) the AI instance has been applied to. */
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;

    /** A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set
     * back to the maxRangedAttackTime. */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int targetTimeLost;
    private int minRangedAttackTime;

    /** The maximum time the AI has to wait before performing another ranged attack. */
    private int maxRangedAttackTime;
    private float attackRange;
    private float followDistance;

    public AIRangedAttack(IRangedAttackMob rangeAttacker, double moveSpeed, int attackTime, float attackRange)
    {
        this(rangeAttacker, moveSpeed, attackTime, attackTime, attackRange);
    }

    public AIRangedAttack(IRangedAttackMob rangeAttacker, double moveSpeed, int minAttackTime, int maxAttackTime, float attackRange)
    {
        this.rangedAttackTime = -1;

        if (!(rangeAttacker instanceof EntityLivingBase))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = rangeAttacker;
            this.entityHost = (EntityLiving) rangeAttacker;
            this.entityMoveSpeed = moveSpeed;
            this.minRangedAttackTime = minAttackTime;
            this.maxRangedAttackTime = maxAttackTime;
            this.attackRange = attackRange;
            this.followDistance = attackRange * attackRange;
            this.setMutexBits(3);
        }
    }

    /** Returns whether the EntityAIBase should begin execution. */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            return true;
        }
    }

    /** Returns whether an in-progress EntityAIBase should continue executing */
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /** Resets the task */
    public void resetTask()
    {
        this.attackTarget = null;
        this.targetTimeLost = 0;
        this.rangedAttackTime = -1;
    }

    /** Updates the task */
    public void updateTask()
    {
        double distanceFromTarget = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
        boolean canSeeTarget = this.entityHost.getEntitySenses().canSee(this.attackTarget);

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
            this.entityHost.getNavigator().clearPathEntity();
        }
        else
        {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0)
        {
            if (distanceFromTarget > (double) this.followDistance || !canSeeTarget)
            {
                return;
            }

            f = MathHelper.sqrt_double(distanceFromTarget) / this.attackRange;

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f);
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = MathHelper.sqrt_double(distanceFromTarget) / this.attackRange;
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
        }
    }
}
