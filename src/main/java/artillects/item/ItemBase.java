package artillects.item;

import artillects.Artillects;
import artillects.ArtillectsTab;

public class ItemBase extends calclavia.lib.prefab.item.ItemBase {

	public ItemBase(int id, String name) {
		super(id, name, Artillects.CONFIGURATION, Artillects.PREFIX, ArtillectsTab.instance());
	}

}
