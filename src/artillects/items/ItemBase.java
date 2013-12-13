package artillects.items;

import artillects.ArtillectsTab;
import artillects.Artillects;
import net.minecraft.item.Item;

/**
 * Base item for all items in this mod
 * 
 * @author Dark
 */
public class ItemBase extends Item
{

	public ItemBase(String name)
	{
		super(Artillects.CONFIGURATION.getItem(name, Artillects.nextItemID()).getInt());
		this.setUnlocalizedName(Artillects.PREFIX + name);
		this.setCreativeTab(ArtillectsTab.instance());
	}

}
