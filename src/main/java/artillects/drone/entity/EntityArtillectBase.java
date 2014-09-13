package artillects.drone.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import artillects.core.Artillects;
import artillects.drone.hive.HiveComplex;
import artillects.drone.hive.zone.Zone;

import com.google.common.io.ByteArrayDataInput;
import resonant.lib.network.handle.IPacketReceiver;
import universalelectricity.core.transform.vector.IVector3;
import universalelectricity.core.transform.vector.IVectorWorld;
import universalelectricity.core.transform.vector.Vector3;

public class EntityArtillectBase extends EntityCreature implements IArtillect, IRangedAttackMob, IVectorWorld
{
    private Zone assignedZone;
    private Object owner;
    private boolean isPlayerOwned;

    public EntityArtillectBase(World world)
    {
        super(world);
    }

    @Override
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (super.attackEntityFrom(source, damage))
        {
            Entity attacker = source.getEntity();

            if (this.riddenByEntity != attacker && this.ridingEntity != attacker)
            {
                if (attacker != this)
                {
                    this.entityToAttack = attacker;
                }

                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
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

    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public void setOwner(Object hive)
    {
        this.owner = hive;
    }

    @Override
    public Object getOwner()
    {
        return this.owner;
    }

    @Override
    public Zone getZone()
    {
        return this.assignedZone;
    }

    @Override
    public void setZone(Zone zone)
    {
        this.assignedZone = zone;
    }

    @Override
    public IInventory getInventory()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("playerOwned", this.isPlayerOwned);
        if (this.getOwner() instanceof HiveComplex)
        {
            nbt.setString("hiveID", ((HiveComplex) this.getOwner()).getName());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.isPlayerOwned = nbt.getBoolean("playerOwned");
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3((IVector3)this).add(0, 0.2, 0), new Vector3(entity).add(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
        super.dropFewItems(par1, par2);
        if (par1)
        {
            //this.entityDropItem(new ItemStack(Drone.itemParts, 1 + this.worldObj.rand.nextInt(2 + par2), this.worldObj.rand.nextInt(ItemDroneParts.Part.values().length - 1)), 0.0F);
            //this.entityDropItem(new ItemStack(Drone.itemParts, 1 + this.worldObj.rand.nextInt(5 + par2), this.worldObj.rand.nextInt(ItemDroneParts.Part.values().length - 1)), 0.0F);

        }
    }

    @Override
    public boolean getCanSpawnHere()
    {
        //return HiveComplexManager.instance().getClosestComplex(this, 200) != null && super.getCanSpawnHere();
        return true;
    }

    @Override
    public boolean isOnSameTeam(EntityLivingBase entity)
    {
        if (entity instanceof IArtillect && ((IArtillect) entity).getOwner() instanceof HiveComplex)
        {
            if (((HiveComplex) ((IArtillect) entity).getOwner()) == this.getOwner())
            {
                return true;
            }
        }
        if (entity instanceof EntityPlayer)
        {
            if (this.getOwner() instanceof HiveComplex)
            {
                //TODO when access list is added to hive check for username
            }
        }
        return this.isOnTeam(entity.getTeam());
    }

    @Override
    public double z()
    {
        return this.posZ;
    }

    @Override
    public double x()
    {
        return this.posX;
    }

    @Override
    public double y()
    {
        return this.posY;
    }

    @Override
    public World world()
    {
        return this.worldObj;
    }
}
