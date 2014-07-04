package artillects.core.region;

public enum TownType
{
    HUNTER_GATHER(0, 20, 10),
    CAMP(),
    COMPLEX(),
    VILLAGE(),
    TOWN(),
    SMALL_CITY(),
    CITY(),
    LARGE_CITY(),
    METRO_CITY(),
    MEGA_CITY();

    private int level = 1;
    private int population = 100;
    private int size = 10;

    private String description = "";
    private String unlocalizedName = "towntype.name";

    private TownType()
    {

    }

    private TownType(int level, int population, int size)
    {
        this.level = level;
        this.population = population;
        this.size = size;
        description = "towntype." + this.name().toLowerCase().replace("_", "") + ".description";
        unlocalizedName = "towntype." + this.name().toLowerCase().replace("_", "") + ".name";
    }

    /** Level of the town type */
    public int getLevel()
    {
        return this.level;
    }

}
