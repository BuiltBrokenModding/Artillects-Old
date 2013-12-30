package artillects.hive;

import com.builtbroken.minecraft.save.ISaveObj;

import net.minecraft.nbt.NBTTagCompound;

/** Base class for all object that the hive uses that are ghosts for world based objects
 * 
 * @author DarkGuardsman */
public class HiveEntityObject implements ISaveObj
{
    protected long ticks = 0;
    public boolean isInvalid = false;

    /** Called on the first tick. Use this to setup the building */
    public void init()
    {}

    /** Called when the entity updates */
    public void updateEntity()
    {
        ticks++;
        if (ticks == 1)
        {
            this.init();
        }
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 2;
        }
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

    @Override
    public void save(NBTTagCompound nbt)
    {}

    @Override
    public void load(NBTTagCompound nbt)
    {}

}
