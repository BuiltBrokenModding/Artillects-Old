package artillects.core.region;

import resonant.lib.utility.LanguageUtility;

/** Types of towns
 * 
 * @author Darkguardsman */
public enum TownType
{
    HUNTER_GATHER(0, 20, 10),
    CAMP(5, 50, 50),
    COMPLEX(10, 500, 500),
    VILLAGE(15, 1000, 1000),
    TOWN(20, 10000, 3000),
    SMALL_CITY(25, 50000, 5000),
    CITY(30, 100000, 7500),
    LARGE_CITY(35, 500000, 10000),
    METRO_CITY(40, 1000000, 50000),
    MEGA_CITY(45, 10000000, 100000);

    private int level = 1;
    private int population = 100;
    private int size = 10;

    private String description = "";
    private String unlocalizedName = "towntype.name";

    private TownType()
    {
        description = "towntype." + this.name().toLowerCase().replace("_", "") + ".description";
        unlocalizedName = "towntype." + this.name().toLowerCase().replace("_", "") + ".name";
    }

    private TownType(int level, int population, int size)
    {
        this();
        this.level = level;
        this.population = population;
        this.size = size;        
    }

    /** Level of the town type */
    public int getLevel()
    {
        return this.level;
    }

    /** population limiter of the type */
    public int getPopulation()
    {
        return this.population;
    }

    /** Max size from center in blocks */
    public int getMaxSize()
    {
        return size;
    }

    /** Name of the type localized */
    public String getName()
    {
        return LanguageUtility.getLocal(unlocalizedName);
    }
    
    /** Name of the type localized */
    public String getDescription()
    {
        return LanguageUtility.getLocal(description);
    }

}
