package artillects.block;

import com.builtbroken.minecraft.DarkCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import artillects.Artillects;
import artillects.ArtillectsTab;

public class BlockBase extends Block
{

	public BlockBase(String name, Material material)
	{
		super(Artillects.CONFIGURATION.getBlock(name, DarkCore.getNextID()).getInt(), material);
		this.setUnlocalizedName(Artillects.PREFIX + name);
		this.setTextureName(Artillects.PREFIX + name);
		this.setCreativeTab(ArtillectsTab.instance());
	}

}
