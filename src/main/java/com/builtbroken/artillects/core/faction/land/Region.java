package com.builtbroken.artillects.core.faction.land;

import com.builtbroken.artillects.core.faction.Faction;
import com.builtbroken.artillects.core.faction.FactionManager;
import com.builtbroken.mc.api.ISave;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public abstract class Region implements ISave
{
    protected World world;
    /** Faction who owns this region */
    protected String faction;
    /** Name of the region, doesn't need to be unique but helps */
    protected String name;
    /** Chunks controlled by this region, in theory should never over lap in same layer */
    protected List<ChunkCoordIntPair> controlledChunks = new ArrayList();
    /** Chunks controlled by this region, and are on the edge of the region */
    protected List<ChunkCoordIntPair> edgeChunks = new ArrayList();

    /** Toggle to claim tiles own by other regions, with less power */
    protected boolean claimOwnedTiles = true;
    /** Toggle to claim tiles owned by an enemy faction, with less power */
    protected boolean claimEnemyFactionTiles = true;

    protected Region(World world, String name)
    {
        this.world = world;
        this.setName(name);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if (nbt.hasKey("name"))
        {
            this.setName(nbt.getString("name"));
        }
        if (nbt.hasKey("faction"))
        {
            this.faction = nbt.getString("faction");
        }
        if (nbt.hasKey("chunks"))
        {
            NBTTagCompound tag = nbt.getCompoundTag("chunks");
            if (tag.hasKey("set0"))
            {
                controlledChunks.clear();
                int[] array = tag.getIntArray("set0");
                for (int i = 0; i < array.length; i += 2)
                {
                    controlledChunks.add(new ChunkCoordIntPair(array[i], array[i + 1]));
                }
                refreshEdges();
            }
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        if (name != null && !name.isEmpty())
        {
            nbt.setString("name", name);
        }
        if (faction != null && !faction.isEmpty())
        {
            nbt.setString("faction", faction);
        }
        if (!controlledChunks.isEmpty())
        {
            NBTTagCompound tag = new NBTTagCompound();

            int[] array = new int[controlledChunks.size() * 2];
            int i = 0;
            for (ChunkCoordIntPair pair : controlledChunks)
            {
                array[i++] = pair.chunkXPos;
                array[i++] = pair.chunkZPos;
            }
            tag.setIntArray("set0", array);
            nbt.setTag("chunks", tag);
        }
        return nbt;
    }

    /**
     * Checks if the chunk is on the edge or inside the region
     *
     * @param chunk - chunk, converted to location
     * @return true if it is connected
     */
    protected boolean isChunkConnected(Chunk chunk)
    {
        ChunkCoordIntPair pair = chunk.getChunkCoordIntPair();
        for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
        {
            ChunkCoordIntPair pair2 = new ChunkCoordIntPair(pair.chunkXPos + dir.offsetX, pair.chunkZPos + dir.offsetZ);
            if (controlledChunks.contains(pair2))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the chunk is on the edge or inside the region
     *
     * @param pair - location of the chunk
     * @return true if it is connected
     */
    protected boolean isChunkConnected(ChunkCoordIntPair pair)
    {
        for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
        {
            ChunkCoordIntPair pair2 = new ChunkCoordIntPair(pair.chunkXPos + dir.offsetX, pair.chunkZPos + dir.offsetZ);
            if (controlledChunks.contains(pair2))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Called to attempt to claim a chunk
     *
     * @param pair - chunk as a location
     * @return true if the chunk was claimed
     */
    protected boolean tryToClaimChunk(ChunkCoordIntPair pair)
    {
        Faction faction = FactionManager.getFaction(world, pair);
        if (faction.getID() == this.faction || faction == FactionManager.neutralFaction && FactionManager.claimChunk(world, this.faction, pair))
        {
            //TODO check if another region at this region layer owns the chunk
            //TODO if another region owns it see if we can over power's its control
            //TODO if so claim the region
        }
        return false;
    }

    /**
     * Called to claim a chunk
     *
     * @param chunk       - chunk to claim, will be converted to location
     * @param doEdgeCheck - true will check if the chunk is an edge for the region, as well check chunks next to it
     */
    protected void claimChunk(Chunk chunk, boolean doEdgeCheck)
    {
        claimChunk(chunk.getChunkCoordIntPair(), doEdgeCheck);
    }

    /**
     * Called to claim a chunk
     *
     * @param pair        - chunk to claim, as location
     * @param doEdgeCheck - true will check if the chunk is an edge for the region, as well check chunks next to it
     */
    protected void claimChunk(ChunkCoordIntPair pair, boolean doEdgeCheck)
    {
        if (!controlledChunks.contains(pair))
        {
            controlledChunks.add(pair);
            if (doEdgeCheck)
            {
                int connectedChunks = 0;
                //See if chunks next to use are no longer edges, and pair is not an edge
                for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
                {
                    ChunkCoordIntPair edgePair = new ChunkCoordIntPair(pair.chunkXPos + dir.offsetX, pair.chunkZPos + dir.offsetZ);
                    if (controlledChunks.contains(pair))
                    {
                        connectedChunks++;
                        if (edgeChunks.contains(edgePair))
                        {
                            //See if chunks next to use are no longer edges
                            int edges = 0;
                            for (ForgeDirection dir2 : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
                            {
                                //We just came from this direction
                                if (dir2 != dir.getOpposite())
                                {

                                }
                            }
                            if (edges == 3)
                            {
                                edgeChunks.remove(edgePair);
                            }
                        }
                    }
                }
                //Pair does not have 4 connections so is an edge
                if (connectedChunks < 4)
                {
                    edgeChunks.add(pair);
                }
            }
        }
    }

    /**
     * Called to find all chunks that are on the edge of the region
     */
    public void refreshEdges()
    {
        edgeChunks.clear();
        for (ChunkCoordIntPair pair : controlledChunks)
        {
            for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
            {
                ChunkCoordIntPair pair2 = new ChunkCoordIntPair(pair.chunkXPos + dir.offsetX, pair.chunkZPos + dir.offsetZ);
                if (!controlledChunks.contains(pair2))
                {
                    edgeChunks.add(pair);
                    break;
                }
            }
        }
    }

    /**
     * Called to validate edges to ensure they are still edges.
     */
    public void validateEdges()
    {
        Iterator<ChunkCoordIntPair> it = edgeChunks.iterator();
        while (it.hasNext())
        {
            ChunkCoordIntPair pair = it.next();
            int i = 0;
            for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST})
            {
                ChunkCoordIntPair pair2 = new ChunkCoordIntPair(pair.chunkXPos + dir.offsetX, pair.chunkZPos + dir.offsetZ);
                if (!controlledChunks.contains(pair2))
                {
                    break;
                }
                i++;
            }
            if (i >= 4)
            {
                it.remove();
            }
        }
    }

    public void setFaction(String faction)
    {
        this.faction = faction;
    }

    public Faction getFaction()
    {
        return FactionManager.getFaction(faction);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
