package artillects.hive;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import universalelectricity.api.vector.VectorWorld;

import artillects.hive.complex.HiveComplex;

import calclavia.lib.utility.nbt.NBTUtility;

/** Hive collection that the drones use for logic and collection feed back
 * 
 * @author Dark */
public class HiveComplexManager
{
    public static final int BUILDING_SCAN_UPDATE_RATE = 10;

    /** Main hive instance */
    private static HiveComplexManager PRIMARY_HIVE;

    /** All active drones loaded by this hive instance */

    public HashMap<String, HiveComplex> complexes = new HashMap<String, HiveComplex>();

    private long lastUpdate = 0;

    /** Main hive instance */
    public static HiveComplexManager instance()
    {
        if (PRIMARY_HIVE == null)
        {
            PRIMARY_HIVE = new HiveComplexManager();
            MinecraftForge.EVENT_BUS.register(PRIMARY_HIVE);
        }
        return PRIMARY_HIVE;
    }

    public void addHiveComplex(HiveComplex hiveComplex)
    {
        if (hiveComplex != null && complexes.get(hiveComplex.getName()) == null)
        {
            complexes.put(hiveComplex.getName(), hiveComplex);
        }
    }

    public void callUpdate()
    {
        if (System.currentTimeMillis() - this.lastUpdate < 100)
        {
            //System.out.println("[Hive] Tick.");

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
                        NBTUtility.saveData(new File(NBTUtility.getSaveDirectory(MinecraftServer.getServer().getFolderName()), "hive/" + event.world.provider.dimensionId + "/complex_" + entry.getValue().getName()), "complex.dat", nbt);
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
            System.out.println("Unloading hive complexes from World: " + event.world.provider.dimensionId);
            HashMap<String, HiveComplex> complexCopy = new HashMap();
            complexCopy.putAll(this.complexes);
            for (Entry<String, HiveComplex> entry : complexCopy.entrySet())
            {
                if (event.world.equals(entry.getValue().location.world))
                {
                    entry.getValue().invalidate();
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
        synchronized (complexes)
        {
            File hiveFolder = new File(NBTUtility.getSaveDirectory(MinecraftServer.getServer().getFolderName()), "hive/" + dim);
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
                                NBTTagCompound tag = NBTUtility.loadData(subFile);
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

    }

    public HiveComplex getClosestComplex(VectorWorld pos, int distanceCheck)
    {
        HiveComplex complex = null;
        HashMap<String, HiveComplex> complexCopy = new HashMap();
        complexCopy.putAll(this.complexes);
        for (Entry<String, HiveComplex> entry : complexCopy.entrySet())
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

    public HiveComplex getHive(String string)
    {
        return this.complexes.get(string);
    }
}
