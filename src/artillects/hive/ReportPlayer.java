package artillects.hive;

import artillects.Vector3;
import artillects.entity.EntityDrone;
import net.minecraft.entity.player.EntityPlayer;

public class ReportPlayer extends Report
{
    public ReportPlayer(EntityPlayer player, EntityDrone drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
