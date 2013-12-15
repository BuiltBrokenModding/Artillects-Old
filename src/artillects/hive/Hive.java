package artillects.hive;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import artillects.entity.EntityArtillectBase;
import artillects.entity.IArtillect;
import artillects.hive.schematics.NBTFileHandler;
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
    private List<IArtillect> activeDrones = new ArrayList<IArtillect>();
    private List<IArtillect> droneAwaitingOrders = new ArrayList<IArtillect>();
    private List<Report> inboxReports = new ArrayList<Report>();
    private List<Zone> activeZones = new ArrayList<Zone>();
    private HashMap<String, HiveComplex> activeComplexs = new HashMap<String, HiveComplex>();

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
        // TODO add validation system to reject reports
        if (report != null && !this.inboxReports.contains(report))
        {
            this.inboxReports.add(report);
        }
    }

    @Deprecated
    public void addDrone(EntityArtillectBase drone)
    {
        if (drone != null && !activeDrones.contains(drone))
        {
            activeDrones.add(drone);
        }
    }

    @Deprecated
    public void removeDrone(EntityArtillectBase drone)
    {
        if (drone != null)
        {
            activeDrones.remove(drone);
        }
    }

    /** Called when a drone is created or activated. Then needs to be loaded into the hive collection */
    public void addDrone(IArtillect drone)
    {
        if (drone != null && !activeDrones.contains(drone))
        {
            activeDrones.add(drone);
        }
    }

    /** Called to remove a drone from the hive. Normal when it dies or gets unloaded by the world */
    public void removeDrone(IArtillect drone)
    {
        if (drone != null)
        {
            activeDrones.remove(drone);
        }
    }

    public void addHiveComplex(HiveComplex hiveComplex)
    {
        if (hiveComplex != null && !activeComplexs.containsKey(hiveComplex.name))
        {
            activeComplexs.put(hiveComplex.name, hiveComplex);
        }
    }

    public void addZone(Zone zone)
    {
        if (zone != null && !activeZones.contains(zone))
        {
            activeZones.add(zone);
        }

    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        ticks++;
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 0;
        }

        synchronized (activeComplexs)
        {
            for (Entry<String, HiveComplex> entry : activeComplexs.entrySet())
            {
                if (entry.getValue() != null)
                {
                    if (entry.getValue().isValid())
                    {
                        entry.getValue().updateEntity();
                    }
                    else
                    {
                        entry.getValue().invalidate();
                        activeComplexs.remove(entry.getKey());
                    }
                }
            }
        }
        synchronized (activeZones)
        {
            Iterator<Zone> zoneIt = activeZones.iterator();
            while (zoneIt.hasNext())
            {
                Zone zone = zoneIt.next();
                if (zone.isValid())
                {
                    zone.updateEntity();
                    if (zone.doesZoneNeedWorkers())
                    {
                        Iterator<IArtillect> droneIterator = this.droneAwaitingOrders.iterator();
                        while (droneIterator.hasNext())
                        {
                            IArtillect drone = droneIterator.next();
                            if (zone.canAssignDrone(drone))
                            {
                                zone.assignDrone(drone);
                                if (drone.getZone() == zone)
                                {
                                    droneIterator.remove();
                                }
                            }
                            if (!zone.doesZoneNeedWorkers())
                            {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    zone.invalidate();
                    zoneIt.remove();
                }
            }
        }
        synchronized (activeDrones)
        {
            Iterator<IArtillect> droneIt = activeDrones.iterator();
            while (droneIt.hasNext())
            {
                IArtillect drone = droneIt.next();
                if (drone.getOwner() != this)
                {
                    droneIt.remove();
                }
                else
                {
                    if (!activeZones.contains(drone.getZone()))
                    {
                        drone.setZone(null);
                    }
                    if (drone.getZone() == null)
                    {
                        if (!this.droneAwaitingOrders.contains(drone))
                            this.droneAwaitingOrders.add(drone);
                    }
                }
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {

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
        return 10;
    }

    @ForgeSubscribe
    public void onWorldSave(Save event)
    {
        synchronized (activeComplexs)
        {
            System.out.println("Saving hive to map int world: " + event.world.provider.dimensionId);
            for (Entry<String, HiveComplex> entry : activeComplexs.entrySet())
            {
                if (entry.getValue().isValid() && event.world.equals(entry.getValue().location.world))
                {
                    NBTTagCompound nbt = new NBTTagCompound();
                    entry.getValue().save(nbt);
                    NBTFileHandler.saveFile("complex.dat", new File(NBTFileHandler.getWorldSaveFolder(MinecraftServer.getServer().getFolderName()), "hive/" + event.world.provider.dimensionId + "/complex_" + entry.getValue().name), nbt);
                }
            }
        }
    }

    @ForgeSubscribe
    public void onWorldunLoad(Unload event)
    {
        synchronized (activeComplexs)
        {
            System.out.println("unloading hive peaces from world: " + event.world.provider.dimensionId);
            for (Entry<String, HiveComplex> entry : activeComplexs.entrySet())
            {
                if (event.world.equals(entry.getValue().location.world))
                {
                    entry.getValue().invalidate();
                    activeComplexs.remove(entry.getKey());
                }
            }
        }
    }

    @ForgeSubscribe
    public void onWorldLoad(WorldEvent.Load event)
    {
        this.loadObjectsForDim(event.world.provider.dimensionId);
    }

    /** Temp loads all the villages from file so the manager can record what villages exist */
    public void loadObjectsForDim(int dim)
    {
        File villageFolder = new File(NBTFileHandler.getWorldSaveFolder(MinecraftServer.getServer().getFolderName()), "hive/" + dim);
        if (villageFolder.exists())
        {
            for (File fileEntry : villageFolder.listFiles())
            {
                if (fileEntry.isDirectory() && fileEntry.getName().startsWith("complex"))
                {
                    for (File subFile : fileEntry.listFiles())
                    {
                        if (subFile.getName().equalsIgnoreCase("complex.dat"))
                        {
                            NBTTagCompound tag = NBTFileHandler.loadFile(subFile);

                        }
                    }
                }
            }
        }
        else
        {
            villageFolder.mkdirs();
        }
    }
}
