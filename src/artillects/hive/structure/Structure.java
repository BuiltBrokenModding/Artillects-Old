package artillects.hive.structure;

import net.minecraft.nbt.NBTTagCompound;
import artillects.Vector3;
import artillects.hive.HiveGhost;
import artillects.hive.schematics.Schematic;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class Structure extends HiveGhost
{
    public String name;
    protected Vector3 location;

    Schematic buildingDesign;

    public Structure(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
    }

    public void setSchematic(Schematic design)
    {
        this.buildingDesign = design;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setString("name", name);
        if (buildingDesign != null && buildingDesign.getFileName() != null)
        {
            nbt.setString("design", buildingDesign.getFileName());
        }

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        name = nbt.getName();
        if (nbt.hasKey("design"))
        {
            Schematic shem = new Schematic();
            shem.getFromResourceFolder(nbt.getString("design"));
            this.setSchematic(shem);
        }
    }
}
