package artillects.block;

import net.minecraft.block.material.Material;

public class BlockHiveBlock extends BlockBase implements IHiveBlock
{
	public BlockHiveBlock(String name)
	{
		super(name, Material.rock);
		this.setHardness(32F);
		this.setResistance(1000F);
	}
}
