package com.builtbroken.artillects.core.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * List of AI task the entity will run each tick, List contains Entries for each task
 */
public class AITaskList extends ArrayList<AITaskEntry>
{
    /**
     * Adds an AI task with priority level
     *
     * @param priority
     * @param ai_task
     */
    public void add(int priority, AITask ai_task)
    {
        add(new AITaskEntry(priority, ai_task));
    }

    /**
     * Removes an AI task
     *
     * @param task
     */
    public void remove(AITask task)
    {
        Iterator iterator = this.iterator();

        while (iterator.hasNext())
        {
            AITaskEntry entityaitaskentry = (AITaskEntry) iterator.next();
            AITask entityaibase1 = entityaitaskentry.action;

            if (entityaibase1 == task)
            {
                iterator.remove();
            }
        }
    }

    /**
     * Called to update all task
     */
    public void onUpdateTasks()
    {
        for(AITaskEntry entry : this)
        {
            entry.action.updateTask();
        }
    }
}