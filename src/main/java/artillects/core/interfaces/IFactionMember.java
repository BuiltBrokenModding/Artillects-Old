package artillects.core.interfaces;

import artillects.core.region.Faction;

public interface IFactionMember
{
    Faction getFaction();

    void setFaction(Faction faction);
}
