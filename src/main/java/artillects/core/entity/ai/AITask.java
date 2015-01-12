package artillects.core.entity.ai;

import com.builtbroken.mc.api.IPosWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

/** Task prefab
 * 
 * @author Darkguardsman */
public abstract class AITask extends EntityAIBase implements IPosWorld
{
    private Entity entity;
    
    public AITask(Entity entity)
    {
        this.entity = entity;
    }
    
    /** Entity hosting this task */
    public Entity entity()
    {
        return entity;
    }
    
    /** Is the entity hosting this task alive */
    public boolean isAlive()
    {
        return !entity.isEntityAlive();
    }
    
    @Override
    public World world()
    {
        return entity.worldObj;
    }
    
    @Override
    public double x()
    {
        return entity.posX;
    }
    
    @Override
    public double y()
    {
        return entity.posY;
    }
    
    @Override
    public double z()
    {
        return entity.posZ;
    }
}
