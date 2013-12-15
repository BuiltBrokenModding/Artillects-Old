package artillects.block.teleporter;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.IHiveBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGlyph extends BlockBase implements IHiveBlock
{
	public static final int MAX_GLYPH = 4;
	public static final Icon[] icons = new Icon[MAX_GLYPH];

	public BlockGlyph()
	{
		super("glyph", Material.iron);
		this.setHardness(32F);
		this.setResistance(1000F);
	}

	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return icons[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		for (int i = 0; i < icons.length; i++)
		{
			icons[i] = register.registerIcon(Artillects.PREFIX + "glyph" + i);
		}

		this.blockIcon = icons[0];
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < icons.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
}
