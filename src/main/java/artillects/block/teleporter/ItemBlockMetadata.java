package artillects.block.teleporter;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * ItemBlock class for Glyphs
 * 
 * @author Calclavia
 */
public class ItemBlockMetadata extends ItemBlock
{
	public ItemBlockMetadata(int par1)
	{
		super(par1);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return super.getUnlocalizedName(itemStack) + "." + itemStack.getItemDamage();
	}
}
