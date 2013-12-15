package artillects.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.Artillects;

public class BlockDecoration extends BlockBase
{
	public BlockDecoration(String name)
	{
		super(name, Material.rock);
		this.setHardness(32F);
		this.setResistance(1000F);
	}
}
