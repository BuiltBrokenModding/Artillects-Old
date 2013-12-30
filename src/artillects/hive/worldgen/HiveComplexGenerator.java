package artillects.hive.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import universalelectricity.api.vector.VectorWorld;
import artillects.hive.HiveComplex;
import artillects.hive.HiveComplexManager;
import cpw.mods.fml.common.IWorldGenerator;

public class HiveComplexGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;
		VectorWorld pos = new VectorWorld(world, chunkX, 6, chunkZ);
		
		if (HiveComplexManager.instance().getClosestComplex(pos, 400) == null)
		{
			HiveComplex complex = new HiveComplex("HiveTX" + System.currentTimeMillis(), pos);
			complex.loadGeneralBuilding(true);
		}
	}
}
