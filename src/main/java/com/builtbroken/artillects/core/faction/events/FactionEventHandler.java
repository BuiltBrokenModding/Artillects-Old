package com.builtbroken.artillects.core.faction.events;

import com.builtbroken.artillects.core.faction.FactionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class FactionEventHandler
{
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event)
    {
        if(!event.world.isRemote)
        {
            if (FactionManager.getMapForDim(event.world.provider.dimensionId, false) == null)
            {
                FactionManager.loadMapFromSave(event.world.provider.dimensionId);
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        if(!event.world.isRemote)
        {
            FactionManager.unloadMapForDim(event.world.provider.dimensionId);
        }
    }

    //@SubscribeEvent
    public void worldSpawn(WorldEvent.PotentialSpawns event)
    {
        //TODO block spawns inside of towns
    }
}
