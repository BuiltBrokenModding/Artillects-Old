package com.builtbroken.artillects.core.entity.profession;

import com.builtbroken.mc.api.ISave;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/** Prefab for task generated tasks that follow a single profession set */
public abstract class Profession<E extends Entity> implements ISave
{
    private E entity;

    public Profession(E entity)
    {
        this.entity = entity;
    }

    /**
     * Load AI tasks unique to this profession
     */
    public abstract void loadAITasks();

    /**
     * Remove AI task unique to this profession.
     * Only called on switch profession.
     */
    public abstract void unloadAITask();

    public E entity()
    {
        return entity;
    }

    public void update()
    {

    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return nbt;
    }

    /**
     * Does the NPC have an active task assigned to complete
     *
     * @return true if there is at minimal something to do
     */
    public boolean hasActiveTask()
    {
        return false;
    }
}
