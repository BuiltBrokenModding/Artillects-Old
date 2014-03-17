package com.builtbroken.ai.combat;

import net.minecraft.entity.Entity;

import com.builtbroken.ai.IEntity;

public interface ICombatEntity extends IEntity
{
    public Entity getTarget();

    public void setTarget(Entity entity);
}
