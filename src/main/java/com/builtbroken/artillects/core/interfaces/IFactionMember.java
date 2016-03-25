package com.builtbroken.artillects.core.interfaces;

import com.builtbroken.artillects.core.faction.Faction;

public interface IFactionMember
{
    Faction getFaction();

    void setFaction(Faction faction);
}
