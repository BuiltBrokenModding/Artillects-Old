package dark.drones.entity;

import net.minecraft.world.World;

/** Used only by the drones enum to make it easier to register new drones
 * 
 * @author Dark */
public interface IDroneBuilder
{
    /** Called to register everything that is need for the drone to be loaded into the game */
    public void register();

    /** Called to create a new instance of the drone */
    public EntityDrone getNew(World world);
}
