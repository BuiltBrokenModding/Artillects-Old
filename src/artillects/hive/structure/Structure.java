package artillects.hive.structure;

import net.minecraft.nbt.NBTTagCompound;
import artillects.Vector3;
import artillects.VectorWorld;
import artillects.hive.HiveGhost;
import artillects.hive.ZoneProcessing;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
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
            //System.out.println("Generating Schematic @ " + location.toString());
            if (building == Building.PROCESSORROOM)
            {
                new ZoneProcessing(location.world, location.subtract(new Vector3(-8, 0, -8)), location.add(new Vector3(8, 5, 8)));
            }
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
