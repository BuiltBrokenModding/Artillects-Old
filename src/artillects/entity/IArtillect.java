package artillects.entity;

import net.minecraft.entity.player.EntityPlayer;
import artillects.hive.zone.Zone;

public interface IArtillect
{
	public void setOwner(EntityPlayer player);

	public Object getOwner();

	public Zone getZone();

	public void setZone(Zone zone);
}
