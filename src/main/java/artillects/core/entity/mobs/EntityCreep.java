package artillects.core.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
/**
 * Version of the MC creeper that comes with custom settings and spawn conditions.
 * 
 * @author Darkguardsman
 */
public class EntityCreep extends EntityMonster
{
    public EntityCreep(World par1World)
    {
        super(par1World);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.creeper.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.creeper.death";
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        if(!this.worldObj.isRemote)
        {
            
            if(this.getHealth() <= (this.getMaxHealth() / 2))
            {
                blowUp(3);
                return true;
            }
        }
        //TODO maybe have creeper bunt attack
        return super.attackEntityAsMob(entity);
    }

    @Override
    protected int getDropItemId()
    {
        //TODO change to wet green grass~ish paste that can be turned into gunpowder
        return Item.gunpowder.itemID;
    }

}
