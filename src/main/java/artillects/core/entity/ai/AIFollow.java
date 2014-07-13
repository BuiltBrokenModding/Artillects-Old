package artillects.core.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import artillects.core.interfaces.IFollower;

/** Modified version of MC's follow task that allows custom follow settings.
 * 
 * @author Darkguardsman */
public class AIFollow extends AITask
{
    float maxDist;
    float minDist;
    double moveSpeed;

    private PathNavigate petPathfinder;
    private int idleTime;
    private boolean avoidsWater;
    private Entity followTarget;

    public AIFollow(EntityLiving entity, double moveSpeed, float minDistance, float maxDistance)
    {
        super(entity);
        this.moveSpeed = moveSpeed;
        this.petPathfinder = entity.getNavigator();
        this.minDist = minDistance;
        this.maxDist = maxDistance;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        Entity owner = ((IFollower) entity()).getFollowTarget();

        if (owner != null)
        {
            if (!(entity().getDistanceSqToEntity(owner) < (double) (this.minDist * this.minDist)))
            {
                followTarget = owner;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() && entity().getDistanceSqToEntity(followTarget) > (double) (this.maxDist * this.maxDist);
    }

    @Override
    public void startExecuting()
    {
        this.idleTime = 0;
        this.avoidsWater = entity().getNavigator().getAvoidsWater();
        entity().getNavigator().setAvoidsWater(false);
    }

    @Override
    public void resetTask()
    {
        followTarget = null;
        this.petPathfinder.clearPathEntity();
        entity().getNavigator().setAvoidsWater(this.avoidsWater);
    }

    @Override
    public void updateTask()
    {
        entity().getLookHelper().setLookPositionWithEntity(followTarget, 10.0F, (float) entity().getVerticalFaceSpeed());

        if (--this.idleTime <= 0)
        {
            this.idleTime = 10;

            if (!this.petPathfinder.tryMoveToEntityLiving(followTarget, this.moveSpeed))
            {
                if (entity().getDistanceSqToEntity(followTarget) >= 144.0D)
                {
                    int i = MathHelper.floor_double(followTarget.posX) - 2;
                    int j = MathHelper.floor_double(followTarget.posZ) - 2;
                    int k = MathHelper.floor_double(followTarget.boundingBox.minY);

                    for (int l = 0; l <= 4; ++l)
                    {
                        for (int i1 = 0; i1 <= 4; ++i1)
                        {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && world().doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !world().isBlockNormalCube(i + l, k, j + i1) && !world().isBlockNormalCube(i + l, k + 1, j + i1))
                            {
                                entity().setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), entity().rotationYaw, entity().rotationPitch);
                                this.petPathfinder.clearPathEntity();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public EntityLiving entity()
    {
        return (EntityLiving) super.entity();
    }
}
