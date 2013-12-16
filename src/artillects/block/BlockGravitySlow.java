package artillects.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import artillects.Artillects;

public class BlockGravitySlow extends BlockBase implements IHiveBlock
{

	public BlockGravitySlow()
	{
		super("gravitySlow", Material.rock);
		setTextureName(Artillects.PREFIX + "gravitySlow");
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity)
	{
		entity.motionX *= 0.4D;
		entity.motionZ *= 0.4D;
	}
}
