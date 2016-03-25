package com.builtbroken.artillects.core.faction;

import com.builtbroken.artillects.core.interfaces.IFactionMember;
import com.builtbroken.jlib.data.vector.IPos2D;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

/**
 * Manager that handles everything related to faction control, setup, and interaction
 *
 * @author Darkguardsman
 */
public class FactionManager
{
    /** Map of faction ids to faction instances */
    private static HashMap<String, Faction> factions = new HashMap();
    /** Map of player names to factions */ //TODO replace with UUIDs
    private static HashMap<String, String> playerToFaction = new HashMap();
    /** Map of dims to faction maps */
    private static HashMap<Integer, FactionMap> dimToFactionMap = new HashMap();
    /** Default faction for all objects */
    private static Faction neutralFaction;

    static
    {
        neutralFaction = Faction.newFaction("Neutral", "World_Neutral");
        addFaction(neutralFaction);
    }

    /**
     * Gets the faction who owns the chunk location
     *
     * @param chunk - chunk, converted to location
     * @return owner of the chunk
     */
    public static Faction getFaction(Chunk chunk)
    {
        if (chunk.worldObj != null && chunk.worldObj.provider != null && dimToFactionMap.containsKey(chunk.worldObj.provider.dimensionId))
        {
            FactionMap map = dimToFactionMap.get(chunk.worldObj.provider.dimensionId);
            String name = map.getFactionForChunk(chunk);
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
                        Engine.logger().error("FactionManager: Error faction map for dim " + chunk.worldObj.provider.dimensionId + " contained entries for unknown faction " + name + ". Trigger remove call to clear faction entries for this unknown faction.");
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
}
