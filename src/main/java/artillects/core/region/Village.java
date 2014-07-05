package artillects.core.region;

import java.io.File;

import net.minecraft.world.World;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import universalelectricity.api.vector.VectorWorld;

/** Small area of buildings grouped together into an area of living.
 * 
 * @author Darkguardsman */
public class Village extends LandController implements IVirtualObject
{
    private TownType townType = null;

    /** Empty constructor should only be used for loading */
    public Village()
    {

    }

    public Village(TownType type, VectorWorld vec, int radius)
    {
        this(vec, radius);
        this.townType = type;
    }

    public Village(VectorWorld vec, int radius)
    {
        this(vec.world, vec.intX(), vec.intY(), vec.intZ(), radius);
    }

    public Village(World world, int x, int y, int z, int radius)
    {
        super(world, x, y, z, radius);
    }

    @Override
    public void init()
    {
        super.init();
        if (getName() == null)
            this.setName("Village");
    }

    /** Sets the type of the town */
    public Village setTownType(TownType type)
    {
        this.townType = type;
        return this;
    }

    /** Type of the town */
    public TownType getType()
    {
        return townType;
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "artillects/areas/" + getID() + "/Village.dat");
    }

    @Override
    public void setSaveFile(File file)
    {
        // TODO Auto-generated method stub

    }
}
