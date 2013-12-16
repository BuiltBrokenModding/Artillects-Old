package artillects.entity;

import artillects.hive.zone.Zone;

public interface IArtillect
{
	public void setOwner(Object hive);

	public Object getOwner();

	public Zone getZone();

	public void setZone(Zone zone);
}
