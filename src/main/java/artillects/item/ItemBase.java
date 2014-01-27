package artillects.item;

import artillects.Artillects;
import artillects.ArtillectsTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;

public class ItemBase extends calclavia.lib.prefab.item.ItemBase {

	public ItemBase(String name) {
		super(Artillects.idManager.getNextItemID(), name, Artillects.CONFIGURATION, Artillects.PREFIX, ArtillectsTab.instance());
	}

}
