package artillects.core.entity;

import universalelectricity.api.vector.IVectorWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** Base entity class for all entities created by artillect mod
 * 
 * @author Darkguardsman */
public class EntityBase extends EntityLiving implements IVectorWorld
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
        this.getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage);
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
            target.setFire(2 * this.worldObj.difficultySetting);
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
}
