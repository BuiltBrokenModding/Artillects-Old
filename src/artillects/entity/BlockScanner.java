package artillects.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.hive.Zone;

/**
 * @author Calclavia
 * 
 */
public class BlockScanner
{
	private World world;
	public HashMap<Block, HashSet<Vector3>> blockPositions = new HashMap<Block, HashSet<Vector3>>();
	private Zone zone;

	public BlockScanner(World world, Zone zone, Set<Block> searchBlocks)
	{
		this.world = world;
		this.zone = zone;

		for (Block block : searchBlocks)
		{
			this.blockPositions.put(block, new HashSet<Vector3>());
		}
	}

	public void calculate()
	{
		Vector3 start = this.zone.start;
		Vector3 end = this.zone.end;

		for (int x = (int) start.x; x < (int) end.x; x++)
		{
			for (int y = (int) start.y; x < (int) end.y; x++)
			{
				for (int z = (int) start.z; x < (int) end.z; x++)
				{
					int blockID = this.world.getBlockId(x, y, z);
					Block block = Block.blocksList[blockID];

					if (block != null && this.blockPositions.containsKey(block))
					{
						this.blockPositions.get(block).add(new Vector3(x, y, z));
					}
				}
			}
		}
	}
}
