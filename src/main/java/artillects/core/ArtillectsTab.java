package artillects.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
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
			itemStack = new ItemStack(Items.iron_door);
		}
		return ArtillectsTab.itemStack;
	}

    @Override
    public Item getTabIconItem()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Items.iron_door);
        }
        return ArtillectsTab.itemStack.getItem();
    }

    public static void setIconItemStack(ItemStack stack)
	{
		ArtillectsTab.itemStack = stack;
	}

}
