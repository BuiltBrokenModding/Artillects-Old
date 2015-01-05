package artillects.core.region;

import artillects.core.building.GhostObject;
import com.builtbroken.mc.lib.transform.vector.IVector3;
import com.builtbroken.mc.lib.transform.vector.IVectorWorld;
import net.minecraft.world.World;

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
