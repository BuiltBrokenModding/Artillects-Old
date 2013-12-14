package artillects;

import net.minecraft.world.World;

public class VectorWorld extends Vector3
{
	public World world;

	public VectorWorld(World world, double x, double y, double z)
	{
		super(x, y, z);
		this.world = world;
	}

	@Override
	public VectorWorld add(double x, double y, double z)
	{
		super.add(x, y, z);
		return this;
	}
}
