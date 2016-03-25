package com.builtbroken.artillects.core.faction;

import com.builtbroken.artillects.core.faction.land.Land;
import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map of chunks and what controls them.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/5/2016.
 */
public class FactionMap implements IVirtualObject
{
    /** DIM ID, never change */
    protected final int dimID;

    /** Map of factions to chunks */
    protected HashMap<String, List<ChunkCoordIntPair>> faction_to_chunks = new HashMap();
    /** Map of chunks to factions that control them */
    protected HashMap<ChunkCoordIntPair, String> chunk_to_factions = new HashMap();
    /** Map of chunks to land's that control them */
    protected HashMap<ChunkCoordIntPair, Land> chunk_to_land = new HashMap();

    //TODO test memory usage of storing pairs and strings, eg look for memory growth, if growth is too high switch methods for storing chunks to faction ids

    //TODO find a way to valid if two factions own the same chunk, only one faction can own a chunk... no sharing with this system

    /**
     * Dimension ID
     *
     * @param dimID - unique dimension that is not already tracked
     */
    public FactionMap(int dimID)
    {
        this.dimID = dimID;
    }

    public void claimChunk(String faction, Chunk chunk)
    {
        if (!faction_to_chunks.containsKey(faction) || faction_to_chunks.get(faction) == null)
        {
            faction_to_chunks.put(faction, new ArrayList<ChunkCoordIntPair>());
        }

        ChunkCoordIntPair pair = chunk.getChunkCoordIntPair();
        List<ChunkCoordIntPair> list = faction_to_chunks.get(faction);
        if (!list.contains(pair))
        {
            list.add(pair);
            chunk_to_factions.put(pair, faction);
            faction_to_chunks.put(faction, list);
        }
    }

    public void claimChunk(String faction, ChunkCoordIntPair pair)
    {
        if (!faction_to_chunks.containsKey(faction) || faction_to_chunks.get(faction) == null)
        {
            faction_to_chunks.put(faction, new ArrayList<ChunkCoordIntPair>());
        }

        List<ChunkCoordIntPair> list = faction_to_chunks.get(faction);
        if (!list.contains(pair))
        {
            list.add(pair);
            chunk_to_factions.put(pair, faction);
            faction_to_chunks.put(faction, list);
        }
    }

    public String getFactionForChunk(Chunk chunk)
    {
        ChunkCoordIntPair pair = chunk.getChunkCoordIntPair();
        if (chunk_to_factions.containsKey(pair))
        {
            return chunk_to_factions.get(pair);
        }
        return null;
    }

    public String getFactionForChunk(ChunkCoordIntPair pair)
    {
        if (chunk_to_factions.containsKey(pair))
        {
            return chunk_to_factions.get(pair);
        }
        return null;
    }

    public void onFactionRemoved(String name)
    {
        if (faction_to_chunks.containsKey(name))
        {
            for (ChunkCoordIntPair pair : faction_to_chunks.get(name))
            {
                chunk_to_factions.remove(pair);
            }
            faction_to_chunks.remove(name);
        }
    }

    public void onFactionNameChange(String previous, String newName)
    {
        if (faction_to_chunks.containsKey(previous))
        {
            List<ChunkCoordIntPair> list = faction_to_chunks.get(previous);
            for (ChunkCoordIntPair pair : list)
            {
                chunk_to_factions.put(pair, newName);
            }
            faction_to_chunks.remove(previous);
            faction_to_chunks.put(newName, list);
        }
    }

    /**
     * Removes all entries connected with the provided chunk location data
     *
     * @param chunk - should never be null
     */
    public void remove(Chunk chunk)
    {
        ChunkCoordIntPair pair = chunk.getChunkCoordIntPair();
        if (chunk_to_factions.containsKey(pair))
        {
            String name = chunk_to_factions.get(pair);
            chunk_to_factions.remove(pair);
            if (name != null && faction_to_chunks.get(name) != null)
            {
                List<ChunkCoordIntPair> chunks = faction_to_chunks.get(name);
                chunks.remove(pair);
                faction_to_chunks.put(name, chunks);
            }
        }
    }

    protected final ChunkCoordIntPair getChunkValue(int x, int z)
    {
        return new ChunkCoordIntPair(x >> 4, z >> 4);
    }

    public void unloadAll()
    {
        chunk_to_factions.clear();
    }


    /**
     * Dimension ID this map tracks
     *
     * @return valid dim ID.
     */
    public int dimID()
    {
        return dimID;
    }


    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "bbm/artillects/factions/maps/Faction_map_" + this.dimID());
    }

    @Override
    public void setSaveFile(File file)
    {

    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return world != null && world.provider != null && world.provider.dimensionId == dimID;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if (nbt.hasKey("list"))
        {
            faction_to_chunks.clear();
            chunk_to_factions.clear();
            NBTTagList list = nbt.getTagList("list", 10);
            for (int tagNum = 0; tagNum < list.tagCount(); tagNum++)
            {
                NBTTagCompound tag = list.getCompoundTagAt(tagNum);
                if (tag.hasKey("faction") && tag.hasKey("set0"))
                {
                    String faction = tag.getString("faction");
                    int[] array = tag.getIntArray("set0");
                    List<ChunkCoordIntPair> pairs = new ArrayList();
                    for (int i = 0; i < array.length; i += 2)
                    {
                        pairs.add(new ChunkCoordIntPair(array[i], array[i + 1]));
                    }
                    if (!pairs.isEmpty())
                    {
                        faction_to_chunks.put(faction, pairs);
                    }
                }
            }
            for (Map.Entry<String, List<ChunkCoordIntPair>> entry : faction_to_chunks.entrySet())
            {
                List<ChunkCoordIntPair> chunkList = entry.getValue();
                if (chunkList != null && !chunkList.isEmpty())
                {
                    for (ChunkCoordIntPair pair : chunkList)
                    {
                        chunk_to_factions.put(pair, entry.getKey());
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<String, List<ChunkCoordIntPair>> entry : faction_to_chunks.entrySet())
        {
            //TODO this might get big so we may need a better save method
            //TODO break into arrays of 500 chunks
            List<ChunkCoordIntPair> chunkList = entry.getValue();
            if (chunkList != null && !chunkList.isEmpty())
            {
                int[] array = new int[chunkList.size() * 2];
                int i = 0;
                for (ChunkCoordIntPair pair : chunkList)
                {
                    array[i++] = pair.chunkXPos;
                    array[i++] = pair.chunkZPos;
                }
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("faction", entry.getKey());
                tag.setIntArray("set0", array);
                list.appendTag(tag);
            }
        }
        nbt.setTag("list", list);
        return nbt;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof FactionMap)
        {
            return ((FactionMap) object).dimID == dimID;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "FactionMap[" + dimID + "]";
    }
}
