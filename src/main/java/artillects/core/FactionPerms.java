package artillects.core;

public enum FactionPerms
{
    ALL(""),
    CLAIM("claim");

    private String node;

    FactionPerms()
    {

    }

    FactionPerms(String node)
    {
        this.node = "faction." + node;
    }

    public String node()
    {
        return node;
    }

    @Override
    public String toString()
    {
        return node;
    }
}
