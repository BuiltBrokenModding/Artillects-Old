package artillects.hive.structure;

import net.minecraft.nbt.NBTTagCompound;
import artillects.VectorWorld;
import artillects.hive.HiveGhost;
import artillects.hive.schematics.Schematic;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class Structure extends HiveGhost
{
    public Building building;
    protected VectorWorld location;

    Schematic buildingDesign;

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
        if (!this.generated)
        {
            this.generated = true;
            buildingDesign.build(location);
        }
    }

    public void setSchematic(Schematic design)
    {
        this.buildingDesign = design;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setInteger("buildingID", building.ordinal());
        if (buildingDesign != null && buildingDesign.getFileName() != null)
        {
            nbt.setString("design", buildingDesign.getFileName());
        }
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        building = Building.values()[nbt.getInteger("buildingID")];
        if (nbt.hasKey("design"))
        {
            Schematic shem = new Schematic();
            shem.getFromResourceFolder(nbt.getString("design"));
            this.setSchematic(shem);
        }
    }
}
