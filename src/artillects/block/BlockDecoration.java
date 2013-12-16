package artillects.block;

import net.minecraft.block.material.Material;

public class BlockDecoration extends BlockBase implements IHiveBlock
{
	public BlockDecoration(String name)
	{
		super(name, Material.rock);
		this.setHardness(32F);
		this.setResistance(1000F);
	}
}
