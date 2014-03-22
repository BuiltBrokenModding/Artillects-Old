package artillects.drone.world;

import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

public class AreaScanner
{
    /** Scans an area one block at a time returning anything it finds to the selector
     * 
     * @param world - world to scan in
     * @param selector - selector to return information to
     * @param start - starting point normally lowest corner(-,-,-)
     * @param end - end point normally highest corner(+,+,+) */
    public static void scanArea(World world, IBlockSelector selector, Vector3 s, Vector3 e)
    {
        if (world != null && selector != null && s != null && e != null)
        {
            Vector3 start = new Vector3(Math.min(s.x, e.x), Math.min(s.y, e.y), Math.min(s.z, e.z));
            Vector3 end = new Vector3(Math.max(s.x, e.x), Math.max(s.y, e.y), Math.max(s.z, e.z));
            VectorWorld loc = new VectorWorld(world, start);
            for (int x = (int) start.x; x < (int) end.x; x++)
            {
                for (int y = (int) start.y; y < (int) end.y; y++)
                {
                    for (int z = (int) start.z; z < (int) end.z; z++)
                    {
                        loc.x = x;
                        loc.y = y;
                        loc.z = z;
                        selector.onScan(loc);
                    }
                }
            }
        }
    }
}
