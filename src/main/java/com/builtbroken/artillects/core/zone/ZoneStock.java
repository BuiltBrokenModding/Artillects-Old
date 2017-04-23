package com.builtbroken.artillects.core.zone;

import com.builtbroken.artillects.core.integration.UsageManager;
import com.builtbroken.artillects.core.integration.UsageTile;
import com.builtbroken.artillects.core.integration.api.IUsageStorage;
import com.builtbroken.mc.core.asm.ChunkSetBlockEvent;
import com.builtbroken.mc.imp.transform.vector.Pos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zone that is used mainly for storing items and blocks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class ZoneStock extends ZoneWorkplace
{
    //TODO add filtering
    //TODO add push and pull task (move items into and out of stock automatically to another stock)
    //TODO add report system(track items enter and leaving, as well current levels)
    /** List of containers that can be used for storage of items */
    protected HashMap<TileEntity, IUsageStorage> storageContainers = new HashMap();
    protected HashMap<Pos, TileEntity> locationOfStorage = new HashMap();

    /** Toggled to trigger early inventory check */
    protected boolean updateInventories = false;

    /** Custom name shown to users */
    protected String customName;

    protected int spaceLeft = 0;
    protected int totalSpace = 0;

    protected int nextUpdateCheck = 0;

    public ZoneStock(World world, Pos start, Pos end)
    {
        super(world, start, end);
    }

    @Override
    public void init()
    {
        super.init();
        scanForValidContainers();
        nextUpdateCheck = 20 + world().rand.nextInt(300);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(ticks % nextUpdateCheck == 0)
        {
            nextUpdateCheck = 20 + world().rand.nextInt(300);
            calculateValues();
        }
    }

    @SubscribeEvent
    public void chunkUnloadEvent(ChunkEvent.Unload event)
    {
        //TODO if chunk contains tiles inside the work zone unload them
    }

    @SubscribeEvent
    public void chunkLoadEvent(ChunkEvent.Load event)
    {
        //TODO if chunk contains tiles inside the work zone load them
    }

    @SubscribeEvent
    public void chunkSetBlockEvent(ChunkSetBlockEvent event)
    {
        Pos pos = new Pos(event.x, event.y, event.z);
        if(locationOfStorage.containsKey(pos))
        {
            TileEntity tile = locationOfStorage.get(pos);
            IUsageStorage usage = storageContainers.get(tile);
            if(usage != null)
            {
                totalSpace -= usage.getStorageSpace(tile);
                spaceLeft -= usage.getStorageSpace(tile);
            }
            storageContainers.remove(tile);
            locationOfStorage.remove(pos);
        }
        TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
        if(tile != null)
        {
            UsageTile usage = UsageManager.GLOBAL_INSTANCE.getUsageFor(tile);
            if (usage instanceof IUsageStorage)
            {
                storageContainers.put(tile, (IUsageStorage) usage);
                locationOfStorage.put(new Pos(tile), tile);
                totalSpace += ((IUsageStorage)usage).getStorageSpace(tile);
                spaceLeft += ((IUsageStorage)usage).getStorageSpace(tile);
            }
        }
    }

    /**
     * Called to look for valid containers for storage. Mainly comes
     * down to containers that have usage data. As well are marked
     * as storage inventories.
     */
    protected void scanForValidContainers()
    {
        //TODO implement support for double chest using a wrapper object
        storageContainers.clear();
        List<TileEntity> inventories = getInventoryTiles();
        for (TileEntity tile : inventories)
        {
            UsageTile usage = UsageManager.GLOBAL_INSTANCE.getUsageFor(tile);
            if (usage instanceof IUsageStorage)
            {
                storageContainers.put(tile, (IUsageStorage) usage);
                locationOfStorage.put(new Pos(tile), tile);
            }
        }
    }

    protected void calculateValues()
    {
        spaceLeft = 0;
        totalSpace = 0;
        for (Map.Entry<TileEntity, IUsageStorage> entry : storageContainers.entrySet())
        {
            spaceLeft += entry.getValue().getSpaceLeft(entry.getKey());
            totalSpace += entry.getValue().getStorageSpace(entry.getKey());
        }
    }

    /**
     * Called to see if there is enough space
     * left for anything in storage.
     * <p/>aw
     * Value is most likely cached and is not
     * live.
     *
     * @return true if there is space.
     */
    public boolean hasSpaceLeft()
    {
        //TODO implement
        return false;
    }

    /**
     * Called to check if there is enough space to store
     * this item.
     *
     * @param stack - item being stored
     * @return true if has space left for the item
     */
    public boolean hasSpaceFor(ItemStack stack)
    {
        if (hasSpaceLeft())
        {
            //TODO implement
        }
        return false;
    }
}
