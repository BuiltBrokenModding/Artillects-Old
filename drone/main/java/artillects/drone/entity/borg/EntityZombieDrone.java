package artillects.drone.entity.borg;

import net.minecraft.world.World;

/** Zombie that has been converted to serve the hive. Acts in the same way a zombie does when it has
 * no task. However, when needed it will gain some level of intelligence when working on a task.
 * 
 * @author Dark */
public class EntityZombieDrone extends EntityBorg
{
    public EntityZombieDrone(World world)
    {
        super(world);
    }

}
