package artillects;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArtillectsTab extends CreativeTabs
{
	private static ArtillectsTab instance;
	public static ItemStack itemStack;

	public ArtillectsTab()
	{
		super("artillects");
	}

	public static ArtillectsTab instance()
	{
		if (instance == null)
		{
			instance = new ArtillectsTab();
		}
		return instance;
	}

	@Override
	public ItemStack getIconItemStack()
	{
		if (itemStack == null)
		{
			itemStack = new ItemStack(Item.ingotIron);
		}
		return ArtillectsTab.itemStack;
	}

	public static void setIconItemStack(ItemStack stack)
	{
		ArtillectsTab.itemStack = stack;
	}

}
