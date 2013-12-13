package artillects.hive.structure;

import artillects.Vector3;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class Structure
{
    public final String name;
    protected Vector3 location;

    protected long ticks = 0;

    public Structure(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
    }

    /** Called on the first tick. Use this to setup the building */
    public void init()
    {

    }

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

    /** Does the structure get update ticks */
    public boolean isValid()
    {
        return true;
    }

    public void invalidate()
    {

    }
}
