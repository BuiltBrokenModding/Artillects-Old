package com.builtbroken.artillects.core.entity.passive;

import net.minecraft.entity.Entity;

/** Prefab for task generated tasks that follow a single profession set */
public class Profession
{
    private Entity entity;

    public Profession(Entity entity)
    {
        this.entity = entity;
    }
    
    public Entity entity()
    {
        return entity;
    }
}
