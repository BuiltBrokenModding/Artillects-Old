package artillects.core.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/** Advanced version of the zombie that can use any weapon, armor, and understand events. Is drawn to
 * sound, fighting, and smells.
 * 
 * @author Darkguardsman */
public class EntityUndead extends EntityMonster
{
    public EntityUndead(World world)
    {
        super(world);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBreakDoor(this));
        //this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        //this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        //this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        //this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 1.0D, false));
        //this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        //this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(3.0D);
    }

    /** Returns true if the newer Entity AI code should be run */
    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }

    /** Plays step sound at given x, y, z for the entity */
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    /** Returns the item ID for the item the mob drops on death. */
    @Override
    protected int getDropItemId()
    {
        return Item.rottenFlesh.itemID;
    }

    /** Get this Entity's EnumCreatureAttribute */
    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void onKillEntity(EntityLivingBase victum)
    {
        super.onKillEntity(victum);
        if (this.worldObj.difficultySetting >= 2)
        {
            Entity entity = null;
            if (victum instanceof EntityVillager)
            {
                entity = new EntityZombie(this.worldObj);
                ((EntityZombie) entity).onSpawnWithEgg((EntityLivingData) null);
                ((EntityZombie) entity).setVillager(true);
                if (victum.isChild())
                {
                    ((EntityZombie) entity).setChild(true);
                }
            }
            else if (victum instanceof EntityPlayer)
            {
                //entity = new EntityDeadPlayer((EntityPlayer)victum);
            }
            else if (victum instanceof EntityPlayer)
            {
                //entity = new EntityUndead(this.worldObj);
            }
            if (entity != null)
            {
                this.worldObj.removeEntity(victum);
                entity.copyLocationAndAnglesFrom(victum);
                this.worldObj.spawnEntityInWorld(entity);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        //TODO ignore poisons and gas effects
        //TODO if skeleton ignore fire
        //TODO if zombie and dies from fire turn into skeleton
        return super.attackEntityFrom(source, damage);
    }
}
