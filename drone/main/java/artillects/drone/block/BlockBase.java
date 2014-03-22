package artillects.drone.block;

import net.minecraft.block.material.Material;
import artillects.core.ArtillectsTab;
import artillects.drone.Drone;

public class BlockBase extends calclavia.lib.prefab.block.BlockAdvanced implements IHiveBlock
{

	public BlockBase(int id, String name, Material material)
	{
		super(id, material);
		this.setUnlocalizedName(Drone.PREFIX + name);
		this.setTextureName(Drone.PREFIX + name);
		this.setCreativeTab(ArtillectsTab.instance());
	}

}
