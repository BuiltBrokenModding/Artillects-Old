package com.builtbroken.artillects.api;

import net.minecraft.entity.Entity;

/**
 * Applied to entities that can follow other entities.
 */
public interface IFollower
{
    Entity getFollowTarget();
}
