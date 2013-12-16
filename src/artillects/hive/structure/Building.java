package artillects.hive.structure;

import artillects.hive.schematics.Schematic;

public enum Building
{
    TEST("", "[Laggy]Hive complex [A]"),
    TUNNELZ("5x5ZTunnel"),
    TUNNELX("5x5XTunnel"),
    WALLZ("5x5ZWall"),
    WALLX("5x5XWall"),
    TUNNELC("5x5CTunnel"),
    FLOOR("5x5Floor"),
    SKYLIGHT("5x5SkyLight"),
    NODE("5x5TunnelNode"),
    PROCESSORROOM("processorBuilding", "Processor Room");
    public String saveName, toolName;
    public Schematic schematic;
    public boolean makeTool = false;

    private Building(String name)
    {
        this.saveName = name;
    }

    private Building(String name, String toolName)
    {
        this(name);
        this.toolName = toolName;
        this.makeTool = true;
    }

    public Schematic getSchematic()
    {
        if (schematic == null)
        {
            schematic = new Schematic();
            schematic.getFromResourceFolder(saveName);
        }
        return schematic;
    }
}
