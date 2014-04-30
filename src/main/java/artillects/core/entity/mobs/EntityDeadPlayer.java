package artillects.core.entity.mobs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityDeadPlayer extends EntityUndead
{
    public EntityDeadPlayer(World world)
    {
        super(world);
    }
    
    public EntityDeadPlayer(EntityPlayer player)
    {
        super(player.worldObj);
    }

}
