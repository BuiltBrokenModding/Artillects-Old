package artillects.entity.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.entity.EntityArtillectFlying;

public class EntitySeeker extends EntityArtillectFlying
{
    private EntityLivingBase targetedEntity;

    /** Cooldown time between target loss and new target aquirement. */
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;
    protected int targetingRange = 100;

    /** The explosion radius of spawned fireballs. */
    private int explosionStrength = 1;

    public EntitySeeker(World world)
    {
        super(world);
        this.setSize(0.7f, 0.3f);
    }

    @Override
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.prevAttackCounter = this.attackCounter;
        if (this.targetedEntity != null && this.targetedEntity.isDead)
        {
            this.targetedEntity = null;
        }

        if (this.targetedEntity == null || this.aggroCooldown-- <= 0)
        {
            this.targetedEntity = this.getTarget(targetingRange);

            if (this.targetedEntity != null)
            {
                this.aggroCooldown = 20;
            }
        }

        if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < targetingRange * targetingRange)
        {

            if (this.canEntityBeSeen(this.targetedEntity))
            {
                ++this.attackCounter;

                if (this.attackCounter == 20)
                {
                    this.launchFireBall(this.targetedEntity, 2, 1);
                    this.attackCounter = -40;
                }
            }
            else if (this.attackCounter > 0)
            {
                --this.attackCounter;
            }
        }
        else
        {
            this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI;

            if (this.attackCounter > 0)
            {
                --this.attackCounter;
            }
        }
    }

}
