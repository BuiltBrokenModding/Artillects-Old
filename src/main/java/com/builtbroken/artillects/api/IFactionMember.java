package com.builtbroken.artillects.api;

import com.builtbroken.artillects.core.faction.Faction;

/**
 * Applied to Entities and TileEntities to identify what faction they belong to.
 */
public interface IFactionMember
{
    Faction getFaction();

    void setFaction(Faction faction);
}
