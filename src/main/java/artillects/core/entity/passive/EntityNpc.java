package artillects.core.entity.passive;

import net.minecraft.entity.INpc;
import net.minecraft.world.World;
import artillects.core.entity.EntityHumanoid;

/** Prefab for all NPC that artillects will contain including crafters and fighters
 * 
 * @author Darkguardsman */
public class EntityNpc extends EntityHumanoid implements INpc
{
    public EntityNpc(World world)
    {
        super(world);
    }

}
