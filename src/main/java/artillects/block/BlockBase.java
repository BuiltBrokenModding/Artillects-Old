package artillects.block;

import net.minecraft.block.material.Material;
import artillects.Artillects;
import artillects.ArtillectsTab;

public class BlockBase extends calclavia.lib.prefab.block.BlockAdvanced implements IHiveBlock
{

	public BlockBase(int id, String name, Material material)
	{
		super(id, material);
		this.setUnlocalizedName(Artillects.PREFIX + name);
		this.setTextureName(Artillects.PREFIX + name);
		this.setCreativeTab(ArtillectsTab.instance());
	}

}
