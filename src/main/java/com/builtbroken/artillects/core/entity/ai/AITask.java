package com.builtbroken.artillects.core.entity.ai;

import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/** Task prefab
 * 
 * @author Darkguardsman */
public abstract class AITask<E extends Entity> implements IWorldPosition
{
    protected final E host;
    
    public AITask(E host)
    {
        this.host = host;
    }

    public void updateTask() {}
    
    /** Entity hosting this task */
    public E entity()
    {
        return host;
    }
    
    /** Is the entity hosting this task alive */
    public boolean isAlive()
    {
        return !host.isEntityAlive();
    }
    
    @Override
    public World world()
    {
        return host.worldObj;
    }
    
    @Override
    public double x()
    {
        return host.posX;
    }
    
    @Override
    public double y()
    {
        return host.posY;
    }
    
    @Override
    public double z()
    {
        return host.posZ;
    }
}
