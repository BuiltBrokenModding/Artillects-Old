package artillects.core.region;

import java.util.HashMap;

/** Manager that handles everything related to faction control, setup, and interaction
 * 
 * @author Darkguardsman */
public class FactionManager
{
    private HashMap<Integer, Faction> factions = new HashMap<Integer, Faction>();

    public Faction getFaction(int id)
    {
        return factions.get(id);
    }

    public void addFaction(int id)
    {

    }
}
