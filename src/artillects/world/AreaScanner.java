package artillects.world;

import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;

public class AreaScanner
{
    /** Scans an area one block at a time returning anything it finds to the selector
     * 
     * @param world - world to scan in
     * @param selector - selector to return information to
     * @param start - starting point normally lowest corner(-,-,-)
     * @param end - end point normally highest corner(+,+,+) */
    public static void scanArea(World world, IBlockSelector selector, Vector3 start, Vector3 end)
    {
        if (world != null && selector != null && start != null && end != null)
        {

        }
    }
}
