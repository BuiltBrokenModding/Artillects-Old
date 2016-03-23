package com.builtbroken.artillects.core.entity.passive;

import com.builtbroken.artillects.core.entity.EntityHumanoid;
import net.minecraft.entity.INpc;
import net.minecraft.world.World;

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
