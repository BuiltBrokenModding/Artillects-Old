package artillects.hive.structure;

import artillects.hive.schematics.Schematic;

public enum Building
{
    TEST("Test"),
    TUNNELZ("5x5ZTunnel"),
    TUNNELX("5x5XTunnel"),
    WALLZ("5x5ZWall"),
    WALLX("5x5XWall"),
    TUNNELC("5x5CTunnel"),
    FLOOR("5x5Floor"),
    SKYLIGHT("5x5SkyLight"),
    NODE("5x5TunnelNode");
    public String name;
    public Schematic schematic;

    private Building(String name)
    {
        this.name = name;
    }

    public Schematic getSchematic()
    {
        if (schematic == null)
        {
            schematic = new Schematic();
            schematic.getFromResourceFolder(name);
        }
        return schematic;
    }
}
