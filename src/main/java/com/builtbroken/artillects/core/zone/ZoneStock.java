package com.builtbroken.artillects.core.zone;

import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.world.World;

/**
 * Zone that is used mainly for storing items and blocks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class ZoneStock extends ZoneWorkplace
{
    public ZoneStock(World world, Pos start, Pos end)
    {
        super(world, start, end);
    }
}
