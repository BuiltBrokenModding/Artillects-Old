package artillects.hive.structure;

import artillects.hive.schematics.Schematic;

public enum Building
{
    TEST("Test"),
    TUNNELZ("ZTunnel"),
    TUNNELX("XTunnel"),
    TUNNELY("YTunnel"),
    TUNNELC("CTunnel");
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
