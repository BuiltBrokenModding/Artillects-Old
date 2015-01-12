package artillects.core.entity;

import artillects.core.Artillects;
import artillects.core.zone.Zone;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IPosWorld;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityArtillectBase extends EntityCreature implements IRangedAttackMob, IPosWorld
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
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("playerOwned", this.isPlayerOwned);
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
        Artillects.proxy.renderLaser(this.worldObj, new Pos((IPos3D)this).add(0, 0.2, 0), new Pos(entity).add(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

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
