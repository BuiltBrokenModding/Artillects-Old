package artillects.drone.entity.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.drone.entity.EntityArtillectFlying;

public class EntitySeeker extends EntityArtillectFlying
{
    private EntityLivingBase targetedEntity;

    public int missileCounter, laserCounter;
    protected int targetingRange = 43;

    public EntitySeeker(World world)
    {
        super(world);
        this.setSize(0.7f, 0.3f);
    }

    @Override
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        if (this.targetedEntity != null && this.targetedEntity.isDead)
        {
            this.targetedEntity = null;
        }

        if (this.targetedEntity == null)
        {
            this.targetedEntity = this.getTarget(targetingRange);
        }

        if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < targetingRange * targetingRange)
        {
            if (this.canEntityBeSeen(this.targetedEntity))
            {
                ++this.missileCounter;
                ++this.laserCounter;
                if (this.missileCounter >= 100)
                {
                    this.launchFireBall(this.targetedEntity, 2, true);
                    this.missileCounter = -40;
                }
                if (this.laserCounter >= 5)
                {
                    this.fireLaser(this.targetedEntity);
                    this.laserCounter = -10;
                }
            }
            else if (this.missileCounter > 0)
            {
                --this.missileCounter;
                --this.laserCounter;
            }
        }
        else
        {
            this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI;

            if (this.missileCounter > 0)
            {
                --this.missileCounter;
            }
            if (this.laserCounter > 0)
            {
                --this.laserCounter;
            }
        }
    }
}
