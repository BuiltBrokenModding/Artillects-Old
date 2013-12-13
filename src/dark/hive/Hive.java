package dark.hive;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

import dark.entity.EntityDrone;

/** Hive collection that the drones use for logic and collection feed back
 * 
 * @author Dark */
public class Hive implements IScheduledTickHandler
{
    public static final int BUILDING_SCAN_UPDATE_RATE = 10;
    /** Main hive instance */
    private static Hive mainHive;
    /** All active drones loaded by this hive instance */
    private List<EntityDrone> activeDrones = new ArrayList<EntityDrone>();

    private long ticks = 0;

    /** Main hive instance */
    public static Hive instance()
    {
        if (mainHive == null)
        {
            mainHive = new Hive();
        }
        return mainHive;
    }

    /** Called when a drone is created or activated. Then needs to be loaded into the hive collection */
    public void addDrone(EntityDrone drone)
    {
        if (drone != null && !activeDrones.contains(drone))
        {
            activeDrones.add(drone);
        }
    }

    /** Called to remove a drone from the hive. Normal when it dies or gets unloaded by the world */
    public void removeDrone(EntityDrone drone)
    {
        if (drone != null)
        {
            activeDrones.remove(drone);
        }
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        // TODO loop threw updates
        ticks++;
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 0;
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (ticks % BUILDING_SCAN_UPDATE_RATE == 0)
        {
            //TODO scan all buildings and process resources
        }

    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel()
    {
        return "Artillects: Hivemind AI";
    }

    @Override
    public int nextTickSpacing()
    {
        return 5;
    }
}
