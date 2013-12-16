package artillects.entity;

import net.minecraft.inventory.IInventory;
import artillects.hive.ArtillectType;
import artillects.hive.zone.Zone;

public interface IArtillect
{
	public void setOwner(Object hive);

	public Object getOwner();

	public Zone getZone();

	public void setZone(Zone zone);

	public ArtillectType getType();

	public void setType(ArtillectType type);

	public IInventory getInventory();
}
