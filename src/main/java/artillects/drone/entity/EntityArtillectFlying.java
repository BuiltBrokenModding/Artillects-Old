package artillects.drone.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.IVectorWorld;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.Artillects;
import artillects.core.entity.ai.TargetSelector;
import artillects.drone.hive.HiveComplex;
import artillects.drone.hive.zone.Zone;

/** Flying entity prefab based on ghast code
 * 
 * @author Darkguardsman */
public class EntityArtillectFlying extends EntityArtillectGround implements IArtillect
{
    public double maxChangeDistance = 16;
    public double maxWaypointDistance = 200;
    public double acceleration = 0.01;
    public double maxSpeed = 0.04;
    public int courseChangeCooldown;
    protected Vector3 waypoint = new Vector3();
    public Object owner;
    private Zone zone;

    public EntityArtillectFlying(World world)
    {
        super(world);
    }

    @Override
    protected boolean isAIEnabled()
    {
        return false;
    }

    @Override
    public void setDead()
    {
        if (this.getOwner() instanceof HiveComplex)
        {
            ((HiveComplex) this.getOwner()).removeDrone(this);
        }
        super.setDead();
    }

    /** Next location this drone will fly too */
    public Vector3 waypoint()
    {
        if (waypoint == null)
        {
            waypoint = new Vector3();
        }
        return waypoint;
    }

    public void createNewWaypoint()
    {
        double groundDistance = 60;
        MovingObjectPosition mop = new VectorWorld((IVectorWorld) this).translate(0, -100, 0).rayTraceBlocks(world(), new Vector3((IVector3) this), true);

        if (mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
        {
            groundDistance = this.posY - mop.blockY;
        }
        System.out.println("GroundDistance: " + groundDistance);
        double dy = (this.rand.nextFloat() * 2.0F - 1.0F) * maxChangeDistance;
        waypoint().y = this.posY + (groundDistance < 50 ? dy : 20 - groundDistance);
        waypoint().y = Math.min(Math.max(waypoint().y, 0), 300);
        waypoint().x = this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * maxChangeDistance;
        waypoint().z = this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * maxChangeDistance;
        System.out.println("Waypoint: " + waypoint());
    }

    protected boolean hasReachedWaypoint(double distance)
    {
        return distance < 1.0D;
    }

    protected boolean isWaypointValid(double distance)
    {
        return !hasReachedWaypoint(distance) && distance < maxWaypointDistance;
    }

    protected float criticallyDampedSpring(float distance, float a_Velocity)
    {
        return criticallyDampedSpring(distance, a_Velocity, 1, 1);
    }

    protected float criticallyDampedSpring(float distance, float a_Velocity, float a_TimeStep, float spring)
    {
        float springForce = distance * spring;
        float dampingForce = (float) (-a_Velocity * 2 * Math.sqrt(spring));
        float force = springForce + dampingForce;
        a_Velocity += force * a_TimeStep;
        float displacement = a_Velocity * a_TimeStep;
        return displacement;
    }

    @Override
    protected void updateEntityActionState()
    {
        this.despawnEntity();
        Vector3 difference = waypoint().clone().difference((IVector3) this);
        System.out.println("Diff: " + difference);
        Vector3 direction = new Vector3(waypoint().toAngle(this));
        System.out.println("Direction: " + direction);
        double distanceToWaypoint = difference.getMagnitude();

        if (isWaypointValid(distanceToWaypoint))
        {
            this.motionX += direction.x * (acceleration);
            this.motionY -= direction.y * (acceleration);
            this.motionZ += direction.z * (acceleration);

            double mX = Math.min(maxSpeed, Math.abs(motionX));
            double mY = Math.min(maxSpeed, Math.abs(motionY));
            double mZ = Math.min(maxSpeed, Math.abs(motionZ));

            this.motionX += motionX < 0 ? -mX : mX;
            this.motionY += motionY < 0 ? -mY : mY;
            this.motionZ += motionZ < 0 ? -mZ : mZ;
        }

        //Move if the timer has cooled down
        if (this.courseChangeCooldown-- <= 0)
        {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;

            if (!isWaypointValid(distanceToWaypoint) || !this.isCourseTraversable(distanceToWaypoint))
            {
                createNewWaypoint();
            }
        }
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {
        if (!this.worldObj.isRemote)
            entityPlayer.mountEntity(this);
        return true;
    }

    @Override
    protected void fall(float par1)
    {
    }

    @Override
    protected void updateFallState(double par1, boolean par3)
    {
    }

    @Override
    public void moveEntityWithHeading(float par1, float par2)
    {
        if (this.isInWater())
        {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.handleLavaMovement())
        {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float f2 = 0.91F;

            if (this.onGround)
            {
                f2 = 0.54600006F;
                int i = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (i > 0)
                {
                    f2 = Block.blocksList[i].slipperiness * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(par1, par2, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround)
            {
                f2 = 0.54600006F;
                int j = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (j > 0)
                {
                    f2 = Block.blocksList[j].slipperiness * 0.91F;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= f2;
            this.motionY *= f2;
            this.motionZ *= f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder()
    {
        return false;
    }

    /** True if the ghast has an unobstructed line of travel to the waypoint. */
    private boolean isCourseTraversable(double distance)
    {
        double d4 = (waypoint().x - this.posX) / distance;
        double d5 = (waypoint().y - this.posY) / distance;
        double d6 = (waypoint().z - this.posZ) / distance;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; i < distance; ++i)
        {
            axisalignedbb.offset(d4, d5, d6);

            if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object getOwner()
    {
        return this.owner;
    }

    @Override
    public void setOwner(Object player)
    {
        this.owner = player;
    }

    @Override
    public Zone getZone()
    {
        return this.zone;
    }

    @Override
    public void setZone(Zone zone)
    {
        this.zone = zone;
    }

    /** Finds a target for the drone to attack */
    public EntityLivingBase getTarget(double range)
    {
        EntityLivingBase entity = null;
        TargetSelector selector = new TargetSelector(this);
        List<EntityLivingBase> entityList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(range, range, range));

        double distance = range * range;
        for (EntityLivingBase currentEntity : entityList)
        {
            double d = new Vector3((IVector3) this).distance(new Vector3(currentEntity));
            if (selector.isEntityApplicable(currentEntity) && d < distance)
            {
                distance = d;
                entity = currentEntity;
            }
        }
        return entity;
    }

    public void launchFireBall(EntityLivingBase targetedEntity, double entityRadius, boolean small)
    {
        double deltaX = targetedEntity.posX - this.posX;
        double deltaY = targetedEntity.boundingBox.minY + targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
        double deltaZ = targetedEntity.posZ - this.posZ;
        this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(deltaX, deltaZ)) * 180.0F / (float) Math.PI;

        this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
        EntityFireball entitylargefireball = small ? new EntitySmallFireball(this.worldObj, this, deltaX, deltaY, deltaZ) : new EntityLargeFireball(this.worldObj, this, deltaX, deltaY, deltaZ);
        Vec3 vec3 = this.getLook(1.0F);
        entitylargefireball.posX = this.posX + vec3.xCoord * entityRadius;
        entitylargefireball.posY = this.posY + this.height / 2.0F + 0.5D;
        entitylargefireball.posZ = this.posZ + vec3.zCoord * entityRadius;
        this.worldObj.spawnEntityInWorld(entitylargefireball);
    }

    public void fireLaser(EntityLivingBase entity)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3((IVector3) this).translate(0, 0.2, 0), new Vector3(entity).translate(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);
    }

    @Override
    public EnumArtillectType getType()
    {
        return EnumArtillectType.SEEKER;
    }

    @Override
    public void setType(EnumArtillectType type)
    {

    }

    @Override
    public IInventory getInventory()
    {
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("type", (byte) this.getType().ordinal());
        nbt.setBoolean("hive", !(this.getOwner() != HiveComplex.getPlayerHive()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        this.setType(EnumArtillectType.values()[nbt.getByte("type")]);
        if (!nbt.getBoolean("hive"))
        {
            HiveComplex.getPlayerHive().addDrone(this);
        }
    }

    @Override
    public boolean getCanSpawnHere()
    {
        //return HiveComplexManager.instance().getClosestComplex(new VectorWorld((IVectorWorld) this), 100) != null && super.getCanSpawnHere();
        return true;
    }

}
