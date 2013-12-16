package artillects.hive;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import artillects.VectorWorld;
import artillects.entity.IArtillect;
import artillects.hive.schematics.NBTFileHandler;
import artillects.hive.zone.Zone;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Hive collection that the drones use for logic and collection feed back
 * 
 * @author Dark
 */
public class Hive implements IScheduledTickHandler
{
	public static final int BUILDING_SCAN_UPDATE_RATE = 10;

	/** Main hive instance */
	private static Hive PRIMARY_HIVE;

	/** All active drones loaded by this hive instance */
	private HashSet<IArtillect> artillects = new HashSet<IArtillect>();

	private HashSet<IArtillect> awaitingOrders = new HashSet<IArtillect>();

	private TreeSet<Report> inboxReports = new TreeSet<Report>();

	public HashSet<Zone> zones = new HashSet<Zone>();

	public HashMap<String, HiveComplex> complexes = new HashMap<String, HiveComplex>();

	private long ticks = 0;

	/** Main hive instance */
	public static Hive instance()
	{
		if (PRIMARY_HIVE == null)
		{
			PRIMARY_HIVE = new Hive();
			MinecraftForge.EVENT_BUS.register(PRIMARY_HIVE);
		}
		return PRIMARY_HIVE;
	}

	/**
	 * Called by a drone or the hive itself to load a report into the system waiting to be processed
	 * into a task
	 */
	public void issueReport(Report report)
	{
		// TODO add validation system to reject reports
		this.inboxReports.add(report);
	}

	/**
	 * Called when a drone is created or activated. Then needs to be loaded into the hive collection
	 */
	public void addDrone(IArtillect drone)
	{
		artillects.add(drone);
		drone.setOwner(this);
	}

	/** Called to remove a drone from the hive. Normal when it dies or gets unloaded by the world */
	public void removeDrone(IArtillect drone)
	{
		artillects.remove(drone);
	}

	public HashSet<IArtillect> getArtillects()
	{
		return this.artillects;
	}

	public void addHiveComplex(HiveComplex hiveComplex)
	{
		if (hiveComplex != null && complexes.get(hiveComplex.name) == null)
		{
			complexes.put(hiveComplex.name, hiveComplex);
		}
	}

	public void addZone(Zone zone)
	{
		this.zones.add(zone);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		this.ticks++;

		if (this.ticks >= Long.MAX_VALUE - 10)
		{
			this.ticks = 0;
		}

		// System.out.println("[Hive] Tick. HiveComplex: " + activeComplexs.size() + " Drones:" +
		// activeDrones.size() + " Zones:" + activeZones.size());
		synchronized (complexes)
		{
			for (Entry<String, HiveComplex> entry : complexes.entrySet())
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
						complexes.remove(entry.getKey());
					}
				}
			}
		}

		synchronized (zones)
		{
			Iterator<Zone> it = zones.iterator();
			while (it.hasNext())
			{
				Zone zone = it.next();
				if (zone.isValid())
				{
					zone.updateEntity();
					if (zone.doesZoneNeedWorkers())
					{
						Iterator<IArtillect> droneIterator = this.awaitingOrders.iterator();
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
					it.remove();
				}
			}
		}
		synchronized (artillects)
		{
			Iterator<IArtillect> droneIt = artillects.iterator();
			while (droneIt.hasNext())
			{
				IArtillect drone = droneIt.next();
				if (drone.getOwner() != this)
				{
					droneIt.remove();
				}
				else
				{
					if (!zones.contains(drone.getZone()))
					{
						drone.setZone(null);
					}
					if (drone.getZone() == null)
					{
						if (!this.awaitingOrders.contains(drone))
							this.awaitingOrders.add(drone);
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
		try
		{
			synchronized (complexes)
			{
				System.out.println("Saving hive to map int world: " + event.world.provider.dimensionId);
				for (Entry<String, HiveComplex> entry : complexes.entrySet())
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@ForgeSubscribe
	public void onWorldunLoad(Unload event)
	{
		try
		{
			synchronized (complexes)
			{
				System.out.println("unloading hive peaces from world: " + event.world.provider.dimensionId);
				for (Entry<String, HiveComplex> entry : complexes.entrySet())
				{
					if (event.world.equals(entry.getValue().location.world))
					{
						entry.getValue().invalidate();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		try
		{
			this.loadObjectsForDim(event.world.provider.dimensionId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Temp loads all the villages from file so the manager can record what villages exist */
	public void loadObjectsForDim(int dim)
	{
		File hiveFolder = new File(NBTFileHandler.getWorldSaveFolder(MinecraftServer.getServer().getFolderName()), "hive/" + dim);
		if (hiveFolder.exists())
		{
			for (File fileEntry : hiveFolder.listFiles())
			{
				if (fileEntry.isDirectory() && fileEntry.getName().startsWith("complex"))
				{
					for (File subFile : fileEntry.listFiles())
					{
						if (subFile.getName().equalsIgnoreCase("complex.dat"))
						{
							NBTTagCompound tag = NBTFileHandler.loadFile(subFile);
							String name = tag.getString("name");
							if (!this.complexes.containsKey(name))
							{
								HiveComplex complex = new HiveComplex();
								complex.load(tag);
								this.complexes.put(name, complex);
							}
						}
					}
				}
			}
		}
		else
		{
			hiveFolder.mkdirs();
		}

	}

	public HiveComplex getClosestComplex(VectorWorld pos, int distanceCheck)
	{
		HiveComplex complex = null;
		for (Entry<String, HiveComplex> entry : this.complexes.entrySet())
		{
			if (entry.getValue() != null && entry.getValue().location.world == pos.world)
			{
				if (complex == null || (entry.getValue().location.distance(pos) < complex.location.distance(pos) && entry.getValue().location.distance(pos) <= distanceCheck))
				{
					complex = entry.getValue();
				}
			}
		}
		return complex;
	}
}
