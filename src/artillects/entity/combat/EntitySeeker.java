package artillects.entity.combat;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.EntityArtillectFlying;
import artillects.entity.ai.EntityDroneSelector;

public class EntitySeeker extends EntityArtillectFlying
{
    private Entity targetedEntity;

    /** Cooldown time between target loss and new target aquirement. */
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;

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
            this.targetedEntity = this.getTarget(100.0D);

            if (this.targetedEntity != null)
            {
                this.aggroCooldown = 20;
            }
        }

        double d4 = 64.0D;

        if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4)
        {
            double d5 = this.targetedEntity.posX - this.posX;
            double d6 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
            double d7 = this.targetedEntity.posZ - this.posZ;
            this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;

            if (this.canEntityBeSeen(this.targetedEntity))
            {
                ++this.attackCounter;

                if (this.attackCounter == 20)
                {
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.worldObj, this, d5, d6, d7);
                    entitylargefireball.field_92057_e = this.explosionStrength;
                    double d8 = 4.0D;
                    Vec3 vec3 = this.getLook(1.0F);
                    entitylargefireball.posX = this.posX + vec3.xCoord * d8;
                    entitylargefireball.posY = this.posY + this.height / 2.0F + 0.5D;
                    entitylargefireball.posZ = this.posZ + vec3.zCoord * d8;
                    this.worldObj.spawnEntityInWorld(entitylargefireball);
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

    public void shootFireBallAt(EntityLivingBase entity)
    {

    }

    /** Finds a target for the drone to attack */
    public EntityLivingBase getTarget(double range)
    {
        EntityLivingBase entity = null;
        EntityDroneSelector selector = new EntityDroneSelector(this);
        List<EntityLivingBase> entityList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(range, range, range));

        double distance = range * range;
        for (EntityLivingBase currentEntity : entityList)
        {
            double d = new Vector3(this).distance(new Vector3(currentEntity));
            if (selector.isEntityApplicable(currentEntity) && d < distance)
            {
                distance = d;
                entity = currentEntity;
            }
        }
        return entity;
    }
}
