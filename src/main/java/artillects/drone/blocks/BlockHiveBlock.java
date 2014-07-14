package artillects.drone.blocks;

import net.minecraft.block.material.Material;

public class BlockHiveBlock extends BlockBase implements IHiveBlock
{
	public BlockHiveBlock(int id, String name)
	{
		super(id, name, Material.rock);
		this.setHardness(32F);
		this.setResistance(1000F);
	}
}
