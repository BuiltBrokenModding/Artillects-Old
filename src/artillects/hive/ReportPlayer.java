package artillects.hive;

import net.minecraft.entity.player.EntityPlayer;
import artillects.Vector3;
import artillects.entity.EntityArtillectBase;

public class ReportPlayer extends Report
{
    public ReportPlayer(EntityPlayer player, EntityArtillectBase drone)
    {
        super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
    }
}
