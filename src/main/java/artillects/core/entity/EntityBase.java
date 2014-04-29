package artillects.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** Base entity class for all entities created by artillect mod
 * 
 * @author Darkguardsman */
public class EntityBase extends EntityLiving
{
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

        //If this entity is on fire and attack with a close ranged hit it sets the target on fire as well
        //TODO add this to onCollide as well
        if (didAttack && this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < this.worldObj.difficultySetting * 0.3F)
        {
            target.setFire(2 * this.worldObj.difficultySetting);
        }

        return didAttack;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
    }
}
