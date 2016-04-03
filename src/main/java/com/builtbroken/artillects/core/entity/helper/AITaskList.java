package com.builtbroken.artillects.core.entity.helper;

import net.minecraft.entity.ai.EntityAIBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of AI task the entity will run each tick, List contains Entries for each task
 */
public class AITaskList extends ArrayList<AITaskEntry>
{
    /** A list of EntityAITaskEntrys that are currently being executed. */
    private List executingTaskEntries = new ArrayList();


    private int tickCount;
    private int tickRate = 3;

    /**
     * Adds an AI task with priority level
     *
     * @param priority
     * @param ai_task
     */
    public void add(int priority, EntityAIBase ai_task)
    {
        add(new AITaskEntry(priority, ai_task));
    }

    /**
     * Removes an AI task
     *
     * @param task
     */
    public void remove(EntityAIBase task)
    {
        Iterator iterator = this.iterator();

        while (iterator.hasNext())
        {
            AITaskEntry entityaitaskentry = (AITaskEntry) iterator.next();
            EntityAIBase entityaibase1 = entityaitaskentry.action;

            if (entityaibase1 == task)
            {
                if (this.executingTaskEntries.contains(entityaitaskentry))
                {
                    entityaibase1.resetTask();
                    this.executingTaskEntries.remove(entityaitaskentry);
                }

                iterator.remove();
            }
        }
    }

    /**
     * Called to update all task
     */
    public void onUpdateTasks()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator;
        AITaskEntry entityaitaskentry;

        if (this.tickCount++ % this.tickRate == 0)
        {
            iterator = this.iterator();

            while (iterator.hasNext())
            {
                entityaitaskentry = (AITaskEntry) iterator.next();
                boolean flag = this.executingTaskEntries.contains(entityaitaskentry);

                if (flag)
                {
                    if (this.canUse(entityaitaskentry) && this.canContinue(entityaitaskentry))
                    {
                        continue;
                    }

                    entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitaskentry);
                }

                if (this.canUse(entityaitaskentry) && entityaitaskentry.action.shouldExecute())
                {
                    arraylist.add(entityaitaskentry);
                    this.executingTaskEntries.add(entityaitaskentry);
                }
            }
        }
        else
        {
            iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext())
            {
                entityaitaskentry = (AITaskEntry) iterator.next();

                if (!entityaitaskentry.action.continueExecuting())
                {
                    entityaitaskentry.action.resetTask();
                    iterator.remove();
                }
            }
        }
        iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            entityaitaskentry = (AITaskEntry) iterator.next();
            entityaitaskentry.action.startExecuting();
        }

        iterator = this.executingTaskEntries.iterator();

        while (iterator.hasNext())
        {
            entityaitaskentry = (AITaskEntry) iterator.next();
            entityaitaskentry.action.updateTask();
        }
    }

    /**
     * Determine if a specific AI Task should continue being executed.
     */
    private boolean canContinue(AITaskEntry task)
    {
        return task.action.continueExecuting();
    }

    /**
     * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
     * tasks are compatible with it or all lower priority tasks can be interrupted.
     */
    private boolean canUse(AITaskEntry entry)
    {
        Iterator iterator = this.iterator();

        while (iterator.hasNext())
        {
            AITaskEntry entityaitaskentry = (AITaskEntry) iterator.next();

            if (entityaitaskentry != entry)
            {
                if (entry.priority >= entityaitaskentry.priority)
                {
                    if (this.executingTaskEntries.contains(entityaitaskentry) && !this.areTasksCompatible(entry, entityaitaskentry))
                    {
                        return false;
                    }
                }
                else if (this.executingTaskEntries.contains(entityaitaskentry) && !entityaitaskentry.action.isInterruptible())
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether two EntityAITaskEntries can be executed concurrently
     */
    private boolean areTasksCompatible(AITaskEntry entry, AITaskEntry entry2)
    {
        return (entry.action.getMutexBits() & entry2.action.getMutexBits()) == 0;
    }


}