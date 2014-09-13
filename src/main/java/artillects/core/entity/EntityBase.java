package artillects.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import universalelectricity.core.transform.vector.IVectorWorld;

/** Base entity class for all entities created by artillect mod
 * 
 * @author Darkguardsman */
public class EntityBase extends EntityCreature implements IVectorWorld
{
    private boolean playerOwned = false;
    
    public EntityBase(World world)
    {
        super(world);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
    public boolean attackEntityAsMob(Entity target)
    {
        boolean didAttack = super.attackEntityAsMob(target);
        if (didAttack && this.getHeldItem() == null && this.isBurning())
        {
            target.setFire(2 * this.worldObj.difficultySetting.ordinal());
        }
        return didAttack;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("playerOwned", this.isPlayerOwned());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setPlayerOwned(nbt.getBoolean("playerOwned"));
    }
    
    public boolean isPlayerOwned()
    {
        return this.playerOwned;
    }
    
    public void setPlayerOwned(boolean b)
    {
        this.playerOwned = b;
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
    
    //--------------------------------
    //--------Helper methods----------
    //--------------------------------
    
    /** Causes the entity to blow up */
    public Explosion blowUp()
    {
        return blowUp(2);
    }
    
    /** Causes the entity to blow up */
    public Explosion blowUp(double size)
    {
        return blowUp(size, true);
    }
    
    /**
     * Causes the entity to blow up
     * @param doDie - should the entity die 
     */
    public Explosion blowUp(double size, boolean doDie)
    {
        Explosion e = causeExplosion(x(), y(), z(), size);
        if(doDie)
        {
            setDead();
        }
        return e;
    }
    
    /**
     * Creates an explosion causes by this entity
     * @param x - x location
     * @param y - y location
     * @param z - z locaion
     * @param size - radius of the explosion
     */
    public Explosion causeExplosion(double x, double y, double z, double size)
    {
        return this.worldObj.createExplosion(this, x, y, z, (float)(size * 2), this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
    }
}
