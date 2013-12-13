package artillects.hive.structure;

import artillects.Vector3;
import artillects.hive.HiveGhost;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class Structure extends HiveGhost
{
    public final String name;
    protected Vector3 location;

    public Structure(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
    }
}
