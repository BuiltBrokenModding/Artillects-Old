package artillects.entity;

import com.builtbroken.ai.IEntity;

import net.minecraft.inventory.IInventory;
import artillects.hive.EnumArtillectType;
import artillects.hive.zone.Zone;

public interface IArtillect extends IEntity
{
	public void setOwner(Object hive);

	public Object getOwner();

	public Zone getZone();

	public void setZone(Zone zone);

	public EnumArtillectType getType();

	public void setType(EnumArtillectType type);

	public IInventory getInventory();
}
