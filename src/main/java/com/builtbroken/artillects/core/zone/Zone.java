package com.builtbroken.artillects.core.zone;

import com.builtbroken.artillects.core.building.GhostObject;
import com.builtbroken.artillects.api.IWorker;
import com.builtbroken.mc.imp.transform.region.Cube;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class used by the hive mind to ID an area by which a task is to be operated in
 *
 * @author DarkGuardsman
 */
public class Zone extends GhostObject
{
    /** Start is always the min point; end is always the largest point. */
    public final World world;
    /** Main, or large box that covers the entire work area */
    public Cube mainArea;

    /** List of workers assigned to the zone */
    public final List<IWorker> workers = new ArrayList();

    public Zone(World world, Pos start, Pos end)
    {
        mainArea = new Cube(start, end);
        this.world = world;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        Iterator<IWorker> it = workers.iterator();
        while (it.hasNext())
        {
            IWorker worker = it.next();
            if (worker.getWorkingZone() != this || ((EntityLivingBase) worker).isDead)
            {
                it.remove();
            }
        }
    }

    /**
     * Checks if the worker can be assigned to the zone
     *
     * @param worker - entity being assigned
     * @return true if the worker can be assigned
     */
    public boolean canAssignWorker(IWorker worker)
    {
        return worker instanceof Entity && !((Entity) worker).isDead;
    }

    /**
     * Assigns a worker to this zone
     *
     * @param worker - entity being assigned
     */
    public void assignWorker(IWorker worker)
    {
        if (worker instanceof Entity && !this.workers.contains(worker))
        {
            this.workers.add(worker);
            worker.setWorkingZone(this);
        }
    }

    /**
     * Does the zone need any workers
     *
     * @return true if zone needs workers
     */
    public boolean doesZoneNeedWorkers()
    {
        return workers.isEmpty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        mainArea = null;
    }

    /**
     * Gets a list of all tiles implementing {@link IInventory}
     *
     * @return list
     */
    public List<IInventory> getInventories()
    {
        List<IInventory> inventories = new ArrayList();
        for (TileEntity tile : getTilesInArea())
        {
            if (tile instanceof IInventory)
            {
                inventories.add((IInventory) tile);
            }
        }
        return inventories;
    }

    public List<TileEntity> getInventoryTiles()
    {
        List<TileEntity> inventories = new ArrayList();
        for (TileEntity tile : getTilesInArea())
        {
            if (tile instanceof IInventory)
            {
                inventories.add(tile);
            }
        }
        return inventories;
    }

    /**
     * Gets a list of all tiles in the defined area
     *
     * @return list of tiles
     */
    public List<TileEntity> getTilesInArea()
    {
        return mainArea.getTilesInArea(world);
    }
}
