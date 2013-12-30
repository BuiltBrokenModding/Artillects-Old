package artillects.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import artillects.entity.EntityArtillectGround;
import artillects.hive.HiveComplex;

public class EntityAIArtillectFollow extends EntityArtillectAIBase
{
    private EntityArtillectGround entity;
    private EntityLivingBase theOwner;
    private PathNavigate petPathfinder;
    private int idleTime;
    float maxDist;
    float minDist;
    private boolean avoidsWater;

    public EntityAIArtillectFollow(EntityArtillectGround entity, double moveSpeed, float minDistance, float maxDistance)
    {
        super(entity.worldObj, moveSpeed);
        this.entity = entity;
        this.petPathfinder = entity.getNavigator();
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setMutexBits(3);
    }

    /** Returns whether the EntityAIBase should begin execution. */
    public boolean shouldExecute()
    {
        Object owner = this.entity.getOwner();

        if (owner instanceof HiveComplex && ((HiveComplex) owner).playerZone)
        {
            EntityLivingBase entitylivingbase = this.entity.worldObj.getClosestPlayer(this.entity.posX, this.entity.posX, this.entity.posX, maxDist);

            if (entitylivingbase != null)
            {
                if (this.entity.getDistanceSqToEntity(entitylivingbase) < (double) (this.minDist * this.minDist))
                {
                    return false;
                }
                else
                {
                    this.theOwner = entitylivingbase;
                    return true;
                }
            }
        }

        return false;
    }

    /** Returns whether an in-progress EntityAIBase should continue executing */
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() && this.entity.getDistanceSqToEntity(this.theOwner) > (double) (this.maxDist * this.maxDist);
    }

    /** Execute a one shot task or start executing a continuous task */
    public void startExecuting()
    {
        this.idleTime = 0;
        this.avoidsWater = this.entity.getNavigator().getAvoidsWater();
        this.entity.getNavigator().setAvoidsWater(false);
    }

    /** Resets the task */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.entity.getNavigator().setAvoidsWater(this.avoidsWater);
    }

    /** Updates the task */
    public void updateTask()
    {
        this.entity.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float) this.entity.getVerticalFaceSpeed());

        if (--this.idleTime <= 0)
        {
            this.idleTime = 10;

            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed))
            {
                if (!this.entity.getLeashed())
                {
                    if (this.entity.getDistanceSqToEntity(this.theOwner) >= 144.0D)
                    {
                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                        int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);

                        for (int l = 0; l <= 4; ++l)
                        {
                            for (int i1 = 0; i1 <= 4; ++i1)
                            {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.world.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.world.isBlockNormalCube(i + l, k, j + i1) && !this.world.isBlockNormalCube(i + l, k + 1, j + i1))
                                {
                                    this.entity.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
                                    this.petPathfinder.clearPathEntity();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public EntityArtillectGround getArtillect()
    {
        return this.entity;
    }
}
