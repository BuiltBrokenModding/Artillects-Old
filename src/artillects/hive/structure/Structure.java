package artillects.hive.structure;

import net.minecraft.nbt.NBTTagCompound;
import artillects.VectorWorld;
import artillects.hive.HiveGhost;

/**
 * Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman
 */
public class Structure extends HiveGhost
{
	public Building building;
	protected VectorWorld location;

	protected boolean generated = false;

	public Structure(Building building, VectorWorld location)
	{
		this.building = building;
		this.location = location;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.generated && building.getSchematic() != null)
		{
			this.generated = true;
			building.getSchematic().build(location);
		}
	}

	@Override
	public void save(NBTTagCompound nbt)
	{
		nbt.setInteger("buildingID", building.ordinal());
	}

	@Override
	public void load(NBTTagCompound nbt)
	{
		building = Building.values()[nbt.getInteger("buildingID")];
	}
}
