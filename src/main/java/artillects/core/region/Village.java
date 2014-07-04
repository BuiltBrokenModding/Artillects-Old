package artillects.core.region;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.building.GhostObject;

/** Small area of buildings grouped together into an area of living.
 * 
 * @author Darkguardsman */
public class Village extends Land implements IVirtualObject
{
    protected int radius = 10;
    private String saveName;

    private Village()
    {

    }

    public Village(VectorWorld vec, int radius)
    {
        this(vec.world, vec.intX(), vec.intY(), vec.intZ(), radius);
    }

    public Village(World world, int x, int y, int z, int radius)
    {
        super(world, x, y, z, radius);
        SaveManager.register(this);
    }

    @Override
    public void init()
    {
        super.init();
        if (getName() == null)
            this.setName("Village");
        if (saveName == null)
            saveName = "Village" + System.currentTimeMillis();
    }

    public static void loadVillageFromSave(NBTTagCompound tag)
    {
        Village village = new Village();
        village.load(tag);
        SaveManager.register(this);
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "artillects/villages/Village_" + this.saveName);
    }

    @Override
    public void setSaveFile(File file)
    {
    }
}
