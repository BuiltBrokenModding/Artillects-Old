package com.builtbroken.ai.combat;

import com.builtbroken.ai.IEntity;

import net.minecraft.entity.Entity;

public interface ICombatEntity extends IEntity
{
    public Entity getTarget();

    public void setTarget(Entity entity);
}
