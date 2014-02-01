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

/** Hive complex manager mainly used for saving control and grouping of hive complexes.
 * 
 * @author DarkGuardsman */
public class HiveComplexManager
{
    /** Main hive instance */
    private static HiveComplexManager instance;

    /** All complex object currently loaded */
    public HashMap<String, HiveComplex> complexes = new HashMap<String, HiveComplex>();

    /** Main instance of the hive complex manager */
    public static HiveComplexManager instance()
    {
        if (instance == null)
        {
            instance = new HiveComplexManager();
            MinecraftForge.EVENT_BUS.register(instance);
        }
        return instance;
    }

    /** Registers a hive complex for tracking */
    public void register(HiveComplex hiveComplex)
    {
        if (hiveComplex != null && getHive(hiveComplex.getName()) == null)
        {
            complexes.put(hiveComplex.getName(), hiveComplex);
        }
    }

    /** Called by a hive complex when it unloads from the map. Mainly used to null the reference in
     * the complex map */
    public void unloadHive(HiveComplex hiveComplex)
    {
        if (hiveComplex != null && getHive(hiveComplex.getName()) != null)
        {
            complexes.put(hiveComplex.getName(), null);
        }
    }

    /** Grabs a hive my name */
    public HiveComplex getHive(String string)
    {
        return this.complexes.get(string);
    }

    /** Checks if a hive complex string id is in use */
    public boolean isNameInUse(String string)
    {
        return this.complexes.containsKey(string);
    }

    @ForgeSubscribe
    public void onWorldLoad(WorldEvent.Load event)
    {
        this.loadObjectsForDim(event.world.provider.dimensionId);
    }

    /** Temp loads all the villages from file so the manager can record what villages exist */
    public void loadObjectsForDim(int dim)
    {
        try
        {
            synchronized (complexes)
            {
                File hiveFolder = new File(NBTUtility.getSaveDirectory(), "hive/" + dim);
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
        catch (Exception e)
        {
            e.printStackTrace();
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

}
