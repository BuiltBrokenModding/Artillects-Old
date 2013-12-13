package dark.drones.hive;

import net.minecraft.entity.player.EntityPlayer;
import dark.drones.Vector3;
import dark.drones.entity.EntityDrone;

public class ReportPlayer extends Report
{
    public ReportPlayer(EntityPlayer player, EntityDrone drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
