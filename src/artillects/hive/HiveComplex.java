package artillects.hive;

import artillects.Vector3;

public class HiveComplex
{
    protected Vector3 location;
    protected String name;
    
    public HiveComplex(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
    }
}
