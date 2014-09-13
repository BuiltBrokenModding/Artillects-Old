package artillects.core.utility;

import net.minecraft.world.World;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

public class AreaScanner
{
    /** Scans an area one block at a time returning anything it finds to the selector
     * 
     * @param world - world to scan in
     * @param selector - selector to return information to
     * @param s - starting point normally lowest corner(-,-,-)
     * @param e - end point normally highest corner(+,+,+) */
    public static void scanArea(World world, IBlockSelector selector, Vector3 s, Vector3 e)
    {
        if (world != null && selector != null && s != null && e != null)
        {
            Vector3 start = new Vector3(Math.min(s.x(), e.x()), Math.min(s.y(), e.y()), Math.min(s.z(), e.z()));
            Vector3 end = new Vector3(Math.max(s.x(), e.x()), Math.max(s.y(), e.y()), Math.max(s.z(), e.z()));
            VectorWorld loc = new VectorWorld(world, start);
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
