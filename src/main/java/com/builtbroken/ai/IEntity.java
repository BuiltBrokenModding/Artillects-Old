package com.builtbroken.ai;

import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;

/** Used with the AI tasks so the same task can be applied to any object regardless of what it
 * actually is
 * 
 * @author Darkguardsman */
public interface IEntity
{
    /** World the entity is in */
    public World getWorld();

    /** Position the entity is in the world */
    public Vector3 pos();
}
