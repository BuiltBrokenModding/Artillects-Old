package com.builtbroken.artillects.core.integration;

import com.builtbroken.mc.core.Engine;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

/**
 * Handles everything related to usage system. Includes calls to check usages, and register usages.
 * <p/>
 * Can be recreated per group of NPCs if needed. However, {@link #GLOBAL_INSTANCE} should be used in most cases.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class UsageManager
{
    private final HashMap<Class<? extends TileEntity>, UsageTile> TILE_USAGE_MAP = new HashMap();

    public static final UsageManager GLOBAL_INSTANCE = new UsageManager();

    public UsageManager()
    {

    }

    public void registerUsage(Class<? extends TileEntity> clazz, UsageTile usage)
    {
        if (!TILE_USAGE_MAP.containsKey(clazz))
        {
            TILE_USAGE_MAP.put(clazz, usage);
        }
        else
        {
            Engine.logger().error("Attempted to register a usage object " + usage + " to " + clazz + " which is already registered", new RuntimeException());
        }
    }
}
