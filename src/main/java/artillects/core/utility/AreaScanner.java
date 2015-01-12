package artillects.core.utility;

import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.world.World;

public class AreaScanner
{
    /** Scans an area one block at a time returning anything it finds to the selector
     * 
     * @param world - world to scan in
     * @param selector - selector to return information to
     * @param s - starting point normally lowest corner(-,-,-)
     * @param e - end point normally highest corner(+,+,+) */
    public static void scanArea(World world, IBlockSelector selector, Pos s, Pos e)
    {
        if (world != null && selector != null && s != null && e != null)
        {
            Pos start = new Pos(Math.min(s.x(), e.x()), Math.min(s.y(), e.y()), Math.min(s.z(), e.z()));
            Pos end = new Pos(Math.max(s.x(), e.x()), Math.max(s.y(), e.y()), Math.max(s.z(), e.z()));
            Location loc = new Location(world, start);
            for (int x = (int) start.x(); x < (int) end.x(); x++)
            {
                for (int y = (int) start.y(); y < (int) end.y(); y++)
                {
                    for (int z = (int) start.z(); z < (int) end.z(); z++)
                    {
                        loc.x_$eq(x);
                        loc.y_$eq(y);
                        loc.z_$eq(z);
                        selector.onScan(loc);
                    }
                }
            }
        }
    }
}
