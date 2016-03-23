package com.builtbroken.artillects.core.region;

import com.builtbroken.artillects.core.building.GhostObject;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
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

    public FactionObject(IWorldPosition vec)
    {
        super(vec);
    }

    public FactionObject(World world, IPos3D vec)
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
