package artillects.hive.logs;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.api.vector.Vector3;
import artillects.entity.EntityArtillectGround;

/** Log used to track that last know location of a player near the hive
 * 
 * @author DarkGuardsman */
public class DataLogPlayer extends DataLog
{
    public DataLogPlayer(EntityPlayer player, EntityArtillectGround drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
