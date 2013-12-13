package artillects.hive;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import artillects.entity.EntityArtillect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

/** Hive collection that the drones use for logic and collection feed back
 * 
 * @author Dark */
public class Hive implements IScheduledTickHandler
{
    public static final int BUILDING_SCAN_UPDATE_RATE = 10;
    /** Main hive instance */
    private static Hive mainHive;
    /** All active drones loaded by this hive instance */
    private List<EntityArtillect> activeDrones = new ArrayList<EntityArtillect>();
    private List<Report> inboxReports = new ArrayList<Report>();
    private List<Zone> activeZones = new ArrayList<Zone>();

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

    /** Called by a drone or the hive itself to load a report into the system waiting to be processed
     * into a task */
    public void issueReport(Report report)
    {
        //TODO add validation system to reject reports 
        if (report != null && !this.inboxReports.contains(report))
        {
            this.inboxReports.add(report);
        }
    }

    /** Called when a drone is created or activated. Then needs to be loaded into the hive collection */
    public void addDrone(EntityArtillect drone)
    {
        if (drone != null && !activeDrones.contains(drone))
        {
            activeDrones.add(drone);
        }
    }

    /** Called to remove a drone from the hive. Normal when it dies or gets unloaded by the world */
    public void removeDrone(EntityArtillect drone)
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

    @ForgeSubscribe
    public void onDroneDeathEvent(LivingDeathEvent event)
    {
        if (event.entityLiving instanceof EntityArtillect)
        {
            //TODO remove from list and issue report for new drone
        }
    }
}
