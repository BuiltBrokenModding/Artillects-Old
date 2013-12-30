package artillects.hive;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.api.vector.Vector3;
import artillects.entity.EntityArtillectGround;

public class ReportPlayer extends Report
{
	public ReportPlayer(EntityPlayer player, EntityArtillectGround drone)
	{
		super("PF" + System.currentTimeMillis(), "PlayerSpotted", player, drone, new Vector3(player));
	}
}
