package com.builtbroken.artillects.core.entity.helper;

import net.minecraft.entity.ai.EntityAIBase;

/**
 * Copy of {@link net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry} with custom changes.
 */
public class AITaskEntry
{
    /** The EntityAIBase object. */
    public EntityAIBase action;
    /** Priority of the EntityAIBase */
    public int priority;

    public AITaskEntry(int priority, EntityAIBase task)
    {
        this.priority = priority;
        this.action = task;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object instanceof AITaskEntry)
        {
            if (((AITaskEntry) object).action == null && action == null)
            {
                return true;
            }
            return action != null && action.equals(((AITaskEntry) object).action);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return action.hashCode();
    }

    @Override
    public String toString()
    {
        return "AITaskEntry[" + priority + ", " + action + "]";
    }
}