package artillects.drone.item;

import artillects.core.ArtillectsTab;
import artillects.drone.Drone;

public class ItemBase extends calclavia.lib.prefab.item.ItemBase {

	public ItemBase(int id, String name) {
		super(id, name, Drone.CONFIGURATION, Drone.PREFIX, ArtillectsTab.instance());
	}

}
