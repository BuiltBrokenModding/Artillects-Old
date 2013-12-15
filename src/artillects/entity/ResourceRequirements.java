package artillects.entity;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Calclavia
 * 
 */
public class ResourceRequirements
{
	public static final HashMap<String, ItemStack[]> requirements = new HashMap<String, ItemStack[]>();

	static
	{
		requirements.put("HiveWall", new ItemStack[] { new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron) });
	}
}
