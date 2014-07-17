package artillects.core.region;

import net.minecraft.world.World;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.IVectorWorld;
import artillects.core.building.GhostObject;

/** Extended version of the ghost object with faction related info */
public class FactionObject extends GhostObject
{
    private String faction_id = null;

    public FactionObject()
    {

    }
    
    public FactionObject(String id)
    {
        this.faction_id = id;
    }

    public FactionObject(IVectorWorld vec)
    {
        super(vec);
    }

    public FactionObject(World world, IVector3 vec)
    {
        super(world, vec);
    }

    public FactionObject(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    /** Sets the faction id of the object */
    public FactionObject setFactionID(String id)
    {
        this.faction_id = id;
        return this;
    }

    /** Faction id for this object */
    public String getFactionID()
    {
        return faction_id;
    }
}