package artillects.core.entity.mobs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;
/**
 * Small version of the creeper that travels at high speeds and blows up like a grenade 
 * @author Darkguardsman
 *
 */
public class EntityBabyCreeper extends EntityCreeper
{    
    public EntityBabyCreeper(World par1World)
    {
        super(par1World);
    }
    
    @Override
    protected void entityInit()
    {
        super.entityInit();
    }
    
    @Override
    public boolean isChild()
    {
        return true;
    }
    
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.5D);
    }
}
