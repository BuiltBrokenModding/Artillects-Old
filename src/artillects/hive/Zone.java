package artillects.hive;

import artillects.Vector3;

/** Class used by the hive mind to ID an area by which a task is to be operated in
 * 
 * @author DarkGuardsman */
public class Zone extends HiveGhost
{
    public Vector3 start, end;

    public String name;

    public Zone(String name, Vector3 start, Vector3 end)
    {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        start = null;
        end = null;
        name = null;
    }
}
