package artillects.drone.hive;

import net.minecraft.world.World;

/** Hive worlds are instances of the hive mind that only function with in one world.
 * 
 * @author Darkguardsman */
public class HiveWorld
{
    private World world = null;

    public HiveWorld(World world)
    {
        this.world = world;
    }

    public World getWorld()
    {
        return this.world;
    }
    
    public void update()
    {
        synchronized(world)
        {
            
        }
    }
}
