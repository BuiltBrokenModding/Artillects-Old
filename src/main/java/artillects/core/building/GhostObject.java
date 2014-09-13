package artillects.core.building;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import resonant.lib.utility.nbt.ISaveObj;
import universalelectricity.core.transform.vector.IVector3;
import universalelectricity.core.transform.vector.IVectorWorld;

/** Base class for all object that the hive uses that are ghosts for world based objects
 * 
 * @author DarkGuardsman */
public class GhostObject implements ISaveObj, IVectorWorld
{
    protected boolean isInvalid = false;
    protected long ticks = 0;
    protected double x, y, z;
    protected World world;
    protected boolean hasLocation = false;
    
    public GhostObject()
    {
        
    }
    
    public GhostObject(IVectorWorld vec)
    {
        this(vec.world(), vec);
    }
    
    public GhostObject(World world, IVector3 vec)
    {
        this(world, vec.x(), vec.y(), vec.z());
    }
    
    public GhostObject(World world, double x, double y, double z)
    {
        this.hasLocation = true;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Called on the first tick. Use this to setup the building */
    public void init()
    {
    }

    /** Called when the entity updates */
    public void updateEntity()
    {
        if (ticks == 0)
        {
            this.init();
        }
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 1;
        }
        ticks++;
    }

    /** Is the entity valid */
    public boolean isValid()
    {
        return !isInvalid;
    }

    /** Called when the zone is invalid or the hive just wants to wipe it out */
    public void invalidate()
    {
        this.isInvalid = true;
    }

    /** Sets the location of the object */
    public GhostObject setLocation(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /** Sets the world of the object */
    public GhostObject setWorld(World world)
    {
        this.world = world;
        return this;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setDouble("x", x());
        nbt.setDouble("y", y());
        nbt.setDouble("z", z());
        if (this.world() != null)
            nbt.setInteger("d", world().provider.dimensionId);

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.x = nbt.getDouble("x");
        this.y = nbt.getDouble("y");
        this.z = nbt.getDouble("z");
        if (nbt.hasKey("d"))
            this.world = DimensionManager.getWorld(nbt.getInteger("d"));
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public double z()
    {
        return z;
    }

    @Override
    public World world()
    {
        return world;
    }

}
