package com.builtbroken.artillects.core.zone;

import com.builtbroken.artillects.core.integration.UsageManager;
import com.builtbroken.artillects.core.integration.UsageTile;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.HashMap;
import java.util.List;

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
    protected HashMap<IInventory, UsageTile> storageContainers = new HashMap();

    /** Toggled to trigger early inventory check */
    protected boolean updateInventories = false;

    /** Custom name shown to users */
    protected String customName;

    public ZoneStock(World world, Pos start, Pos end)
    {
        super(world, start, end);
    }

    @Override
    public void init()
    {
        super.init();
        scanForValidContainers();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @SubscribeEvent
    public void chunkUpdate(BlockEvent event)
    {
        if (event instanceof BlockEvent.BreakEvent || event instanceof BlockEvent.PlaceEvent)
        {
            updateInventories = true;
        }
    }

    @SubscribeEvent
    public void chunkUnloadEvent(ChunkEvent.Unload event)
    {
        //TODO if chunk contains tiles inside the work zone unload them
    }

    protected void scanForValidContainers()
    {
        storageContainers.clear();
        List<IInventory> inventories = getInventoryTiles();
        for (IInventory inventory : inventories)
        {
            UsageTile usage = UsageManager.GLOBAL_INSTANCE.getUsageFor(inventory);
            if (usage != null)
            {
                storageContainers.put(inventory, usage);
            }
        }
    }
}
