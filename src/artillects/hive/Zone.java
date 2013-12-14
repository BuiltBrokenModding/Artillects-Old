package artillects.hive;

import net.minecraft.world.World;
import artillects.Vector3;

/** Class used by the hive mind to ID an area by which a task is to be operated in
 * 
 * @author DarkGuardsman */
public class Zone extends HiveGhost
{
    /** Start is always the min point; end is always the largest point. */
    public Vector3 start, end;

    public World world;

    public Zone(World world, Vector3 start, Vector3 end)
    {
        this.world = world;
        this.start = new Vector3(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z));
        this.end = new Vector3(Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z));
        Hive.instance().addZone(this);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        start = null;
        end = null;
    }
}
