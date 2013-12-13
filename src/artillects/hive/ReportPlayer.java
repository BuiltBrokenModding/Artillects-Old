package artillects.hive;

import artillects.Vector3;
import artillects.entity.EntityArtillectBase;
import net.minecraft.entity.player.EntityPlayer;

public class ReportPlayer extends Report
{
    public ReportPlayer(EntityPlayer player, EntityArtillectBase drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
