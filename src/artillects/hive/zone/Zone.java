package artillects.hive.zone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.VectorWorld;
import artillects.entity.IArtillect;
import artillects.hive.HiveComplex;
import artillects.hive.HiveGhost;

/** Class used by the hive mind to ID an area by which a task is to be operated in
 * 
 * @author DarkGuardsman */
public class Zone extends HiveGhost
{
    /** Start is always the min point; end is always the largest point. */
    public VectorWorld start, end;

    public HiveComplex complex;

    public List<IArtillect> assignedDrones = new ArrayList<IArtillect>();

    public Zone(HiveComplex complex, VectorWorld start, VectorWorld end)
    {
        this.start = new VectorWorld(start.world, Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z));
        this.end = new VectorWorld(start.world, Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        Iterator<IArtillect> droneIt = assignedDrones.iterator();
        while (droneIt.hasNext())
        {
            IArtillect drone = droneIt.next();
            if (drone.getZone() != this || ((EntityLivingBase) drone).isDead)
                droneIt.remove();
        }
    }

    public boolean canAssignDrone(IArtillect drone)
    {
        return true;
    }

    public void assignDrone(IArtillect drone)
    {
        if (drone != null && !this.assignedDrones.contains(drone))
        {
            this.assignedDrones.add(drone);
            drone.setZone(this);
        }
    }

    public boolean doesZoneNeedWorkers()
    {
        return assignedDrones.isEmpty();
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

    public HiveComplex getComplex()
    {

        return null;
    }
}
