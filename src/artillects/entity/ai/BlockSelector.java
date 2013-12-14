package artillects.entity.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.hive.Zone;

/**
 * @author Calclavia
 * 
 */
public class BlockSelector
{
	private World world;
	public final HashMap<Block, HashSet<Vector3>> scannedSortedPositions = new HashMap<Block, HashSet<Vector3>>();
	public final ArrayList<Vector3> scannedPositions = new ArrayList<Vector3>();
	private Zone zone;

	public BlockSelector(World world, Zone zone, Block... searchBlocks)
	{
		this(world, zone, Arrays.asList(searchBlocks));
	}

	public BlockSelector(World world, Zone zone, List<Block> searchBlocks)
	{
		this.world = world;
		this.zone = zone;

		for (Block block : searchBlocks)
		{
			this.scannedSortedPositions.put(block, new HashSet<Vector3>());
		}
	}

	
}
