package artillects.block;

import net.minecraft.item.ItemBlock;

/**
 * ItemBlock class for Glyphs
 * 
 * @author Calclavia
 */
public class ItemBlockGlyph extends ItemBlock
{
	public ItemBlockGlyph(int par1)
	{
		super(par1);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
}
