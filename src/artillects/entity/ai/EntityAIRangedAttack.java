package artillects.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAIRangedAttack extends EntityAIBase
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
    private int field_96561_g;

    /** The maximum time the AI has to wait before peforming another ranged attack. */
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float field_82642_h;

    public EntityAIRangedAttack(IRangedAttackMob rangeAttacker, double moveSpeed, int par4, float par5)
    {
        this(rangeAttacker, moveSpeed, par4, par4, par5);
    }

    public EntityAIRangedAttack(IRangedAttackMob rangeAttacker, double moveSpeed, int par4, int par5, float par6)
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
            this.field_96561_g = par4;
            this.maxRangedAttackTime = par5;
            this.field_96562_i = par6;
            this.field_82642_h = par6 * par6;
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

        if (distanceFromTarget <= (double) this.field_82642_h && this.targetTimeLost >= 20)
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
            if (distanceFromTarget > (double) this.field_82642_h || !canSeeTarget)
            {
                return;
            }

            f = MathHelper.sqrt_double(distanceFromTarget) / this.field_96562_i;
            float f1 = f;

            if (f < 0.1F)
            {
                f1 = 0.1F;
            }

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = MathHelper.sqrt_double(distanceFromTarget) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(f * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
        }
    }
}
