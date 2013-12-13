package artillects.hive;

import artillects.Vector3;
import artillects.entity.EntityArtillect;
import net.minecraft.entity.player.EntityPlayer;

public class ReportPlayer extends Report
{
    public ReportPlayer(EntityPlayer player, EntityArtillect drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
