package artillects.drone.items;

import artillects.core.ArtillectsTab;
import artillects.core.Reference;
import artillects.drone.Drone;

public class ItemBase extends calclavia.lib.prefab.item.ItemBase
{

    public ItemBase(int id, String name)
    {
        super(id, name, Drone.CONFIGURATION, Reference.PREFIX, ArtillectsTab.instance());
    }

}
