package artillects.hive.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import artillects.VectorWorld;
import artillects.hive.Hive;
import artillects.hive.HiveComplex;
import cpw.mods.fml.common.IWorldGenerator;

public class HivecomplexGenerator implements IWorldGenerator
{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;
		VectorWorld pos = new VectorWorld(world, chunkX, 63 - 32, chunkZ);
		if (Hive.instance().getClosestComplex(pos, 400) == null)
		{
			HiveComplex complex = new HiveComplex("HiveTX" + System.currentTimeMillis(), pos);
			complex.loadGeneralBuilding(true);
		}
	}
}
