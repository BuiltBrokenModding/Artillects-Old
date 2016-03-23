package com.builtbroken.artillects.core.zone;

import com.builtbroken.artillects.core.building.GhostObject;
import com.builtbroken.artillects.core.interfaces.IWorker;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Class used by the hive mind to ID an area by which a task is to be operated in
 * 
 * @author DarkGuardsman */
public class Zone extends GhostObject
{
    /** Start is always the min point; end is always the largest point. */
    public World world;
    public Pos start, end;

    public List<IWorker> assignedArtillects = new ArrayList<IWorker>();

    public Zone(World world, Pos start, Pos end)
    {
        this.start = new Pos(Math.min(start.x(), end.x()), Math.min(start.y(), end.y()), Math.min(start.z(), end.z()));
        this.end = new Pos(Math.max(start.x(), end.x()), Math.max(start.y(), end.y()), Math.max(start.z(), end.z()));
        this.world = world;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        Iterator<IWorker> droneIt = assignedArtillects.iterator();
        while (droneIt.hasNext())
        {
            IWorker drone = droneIt.next();
            if (drone.getWorkingZone() != this || ((EntityLivingBase) drone).isDead)
            {
                droneIt.remove();
            }
        }
    }

    public boolean canAssignDrone(IWorker drone)
    {
        return true;
    }

    public void assignArtillect(IWorker drone)
    {
        if (drone != null && !this.assignedArtillects.contains(drone))
        {
            this.assignedArtillects.add(drone);
            drone.setWorkingZone(this);
        }
    }

    public boolean doesZoneNeedWorkers()
    {
        return assignedArtillects.isEmpty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        start = null;
        end = null;
    }

    public void scanForChests()
    {
        // TODO load IInventory tiles from chunks into a list for easy access
    }
}
