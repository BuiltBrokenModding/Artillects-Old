package com.builtbroken.artillects.core.faction;

import com.builtbroken.artillects.Artillects;
import com.builtbroken.artillects.api.IFactionMember;
import com.builtbroken.jlib.data.vector.IPos2D;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.lib.helper.NBTUtility;
import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager that handles everything related to faction control, setup, and interaction
 *
 * @author Darkguardsman
 */
public class FactionManager
{
    /** Map of faction ids to faction instances */
    private static HashMap<String, Faction> factions = new HashMap();
    /** Map of player names to player's main faction */ //TODO replace with UUIDs
    private static HashMap<String, String> playerToFaction = new HashMap();
    /** Map of dims to faction maps */
    private static HashMap<Integer, FactionMap> dimToFactionMap = new HashMap();
    /** Default faction for all objects */
    public static Faction neutralFaction;

    static
    {
        neutralFaction = Faction.newFaction("Neutral", "World_Neutral");
        addFaction(neutralFaction);
    }

    /**
     * Gets the faction who owns the chunk location
     *
     * @return owner of the chunk
     */
    public static Faction getFaction(World world, ChunkCoordIntPair pair)
    {
        if (world != null && world.provider != null && dimToFactionMap.containsKey(world.provider.dimensionId))
        {
            FactionMap map = dimToFactionMap.get(world.provider.dimensionId);
            String name = map.getFactionForChunk(pair);
            if (name != null && !name.isEmpty())
            {
                if (factions.containsKey(name))
                {
                    return factions.get(name);
                }
                else
                {
                    if (Engine.runningAsDev)
                    {
                        Engine.logger().error("FactionManager: Error faction map for dim " + world.provider.dimensionId + " contained entries for unknown faction " + name + ". Trigger remove call to clear faction entries for this unknown faction.");
                    }
                    map.onFactionRemoved(name);
                }
            }
        }
        return neutralFaction;
    }

    /**
     * Gets a faction instance by it's ID
     *
     * @param id - string id, not the display name
     * @return faction instance if one exists
     */
    public static Faction getFaction(String id)
    {
        return factions.get(id);
    }

    /**
     * Gets faction for the entity
     *
     * @param entity - entity in question
     * @return faction the entity belongs to
     */
    public static Faction getFaction(Entity entity)
    {
        if (entity instanceof IFactionMember)
        {
            return ((IFactionMember) entity).getFaction();
        }
        if (entity instanceof EntityPlayer)
        {
            if (playerToFaction.containsKey(entity.getCommandSenderName()))
            {
                String id = playerToFaction.get(entity.getCommandSenderName());
                if (factions.containsKey(id))
                {
                    return factions.get(id);
                }
            }
        }
        return neutralFaction;
    }

    /**
     * Gets a faction based the location
     *
     * @param vec - world location, y is ignored
     * @return faction who owns the location
     */
    public static Faction getFaction(IWorldPosition vec)
    {
        return getFaction(vec.world(), new Point(vec.x(), vec.z()));
    }

    /**
     * Gets a faction based on the location
     *
     * @param world - world to look up the location
     * @param vec   - 3D position, y is ignored
     * @return faction who owns the location
     */
    public static Faction getFaction(World world, IPos3D vec)
    {
        return getFaction(world, (IPos2D) vec);
    }

    /**
     * Gets a faction based on a 2D map
     *
     * @param world - world to look up the location
     * @param vec   - 2D coords, switch y for z
     * @return faction who owns the location
     */
    public static Faction getFaction(World world, IPos2D vec)
    {
        if (world != null && world.provider != null && dimToFactionMap.containsKey(world.provider.dimensionId))
        {
            FactionMap map = dimToFactionMap.get(world.provider.dimensionId);
            String name = map.getFactionForChunk(map.getChunkValue((int) vec.x(), (int) vec.y()));
            if (name != null && !name.isEmpty())
            {
                if (factions.containsKey(name))
                {
                    return factions.get(name);
                }
                else
                {
                    if (Engine.runningAsDev)
                    {
                        Engine.logger().error("FactionManager: Error faction map for dim " + world.provider.dimensionId + " contained entries for unknown faction " + name + ". Trigger remove call to clear faction entries for this unknown faction.");
                    }
                    map.onFactionRemoved(name);
                }
            }
        }
        return neutralFaction;
    }

    /**
     * Adds a faction to the global tracker
     *
     * @param faction - faction to add, needs a unique id
     * @return true if added
     */
    public static boolean addFaction(Faction faction)
    {
        if (getFaction(faction.getID()) == null)
        {
            factions.put(faction.getID(), faction);
            SaveManager.register(faction);
            return true;
        }
        return false;
    }

    /**
     * Creates a new player owned faction.
     *
     * @param player
     * @param name
     * @return
     */
    public static Faction create(EntityPlayer player, String name)
    {
        //TODO do checks if id exists
        Faction faction = Faction.newFaction(player, name);
        addFaction(faction);
        return faction;
    }

    /**
     * Creates a new faction, should only be
     * used to create NPC factions.
     *
     * @param name
     * @return
     */
    public static Faction create(String name)
    {
        //TODO do checks if id exists
        Faction faction = Faction.newFaction(name);
        addFaction(faction);
        return faction;
    }

    public static boolean claimChunk(World world, String faction, ChunkCoordIntPair pair)
    {
        if (factions.containsKey(faction))
        {
            FactionMap map = getMapForDim(world.provider.dimensionId);
            map.claimChunk(faction, pair);
            return true;
        }
        return false;
    }

    public static FactionMap getMapForDim(int dim)
    {
        if (!dimToFactionMap.containsKey(dim))
        {
            dimToFactionMap.put(dim, new FactionMap(dim));
        }
        return dimToFactionMap.get(dim);
    }

    /**
     * Called at server start to load all faction data from disk. Only loads
     * over all faction objects and players. Land claims are loaded and unloaded
     * per world.
     */
    public static void loadFromDisk()
    {
        factions.clear();
        playerToFaction.clear();
        dimToFactionMap.clear();
        File folder = new File(NBTUtility.getSaveDirectory(), "bbm/artillects/factions/");
        if (folder.exists())
        {
            for (File file : folder.listFiles())
            {
                if (file.isFile())
                {
                    String name = file.getName();
                    if (name.endsWith(".dat"))
                    {
                        if (name.startsWith("Faction_"))
                        {
                            NBTTagCompound tag = NBTUtility.loadData(file);
                            if (tag != null && !tag.hasNoTags())
                            {
                                Faction faction = Faction.loadFaction(tag);
                                if (faction.getID() != null)
                                {
                                    if (addFaction(faction))
                                    {
                                        Artillects.logger.info("FactionManager: Loaded faction " + faction.getID() + " from disk");
                                    }
                                    else
                                    {
                                        Artillects.logger.error("FactionManager: Failed loading faction " + faction.getID() + " from disk as it is already loaded");
                                    }
                                }
                            }
                            else
                            {
                                Artillects.logger.error("FactionManager: File " + file + " is named after a faction but is not an NBT save. Deleting to prevent issues....");
                                try
                                {
                                    file.delete();
                                }
                                catch (SecurityException e)
                                {
                                    Artillects.logger.error("FactionManager: Failed to delete file " + file, e);
                                }
                            }
                        }
                        else if (name.startsWith("Players_"))
                        {
                            NBTTagCompound tag = NBTUtility.loadData(file);
                            if (tag != null && !tag.hasNoTags())
                            {
                                NBTTagList players = tag.getTagList("players", 10);
                                for (int i = 0; i < players.tagCount(); i++)
                                {
                                    NBTTagCompound playerTag = players.getCompoundTagAt(i);
                                    if (playerTag.hasKey("username") && playerTag.hasKey("faction"))
                                    {
                                        playerToFaction.put(playerTag.getString("username"), playerTag.getString("faction"));
                                    }
                                    else
                                    {
                                        Artillects.logger.error("FactionManager: Failed to load player data[" + playerTag + "] as it didn't contain a both a faction and username");
                                    }
                                }
                            }
                            else
                            {
                                Artillects.logger.error("FactionManager: File " + file + " is named is labeled as a player data but is not an NBT save. Deleting to prevent issues....");
                                try
                                {
                                    file.delete();
                                }
                                catch (SecurityException e)
                                {
                                    Artillects.logger.error("FactionManager: Failed to delete file " + file, e);
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            folder.mkdirs();
        }
    }

    /**
     * Called to save all data not already saved by other handlers.
     */
    public static void saveData()
    {
        //Factions are saved by SaveManager so no need to save them
        if (!playerToFaction.isEmpty())
        {
            NBTTagList list = new NBTTagList();
            for (Map.Entry<String, String> entry : playerToFaction.entrySet())
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("username", entry.getKey());
                tag.setString("faction", entry.getValue());
                list.appendTag(tag);
            }
            NBTTagCompound save = new NBTTagCompound();
            save.setTag("players", list);
            NBTUtility.saveData(new File(NBTUtility.getSaveDirectory(), "bbm/artillects/factions/PlayerData.dat"), save);
        }
    }
}
