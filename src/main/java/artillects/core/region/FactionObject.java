package artillects.core.region;

import artillects.core.building.GhostObject;

/** Extended version of the ghost object with faction related info */
public class FactionObject extends GhostObject
{
    private String faction_id = null;

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
