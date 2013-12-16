package artillects.hive.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import artillects.Pair;
import artillects.Vector3;
import artillects.entity.EntityWorker;
import artillects.entity.IArtillect;
import artillects.hive.HiveComplex;

public class ZoneMining extends Zone
{
	public final HashMap<Block, HashSet<Vector3>> scannedSortedPositions = new HashMap<Block, HashSet<Vector3>>();

	public final List<Vector3> scannedBlocks = new ArrayList<Vector3>();

	public static final List<Pair<Integer, Integer>> oreList = new ArrayList<Pair<Integer, Integer>>();

	public boolean clearAll = false;

	static
	{
		addBlockToMiningList(Block.oreCoal);
		addBlockToMiningList(Block.oreIron);
		addBlockToMiningList(Block.oreGold);
		addBlockToMiningList(Block.oreEmerald);
		addBlockToMiningList(Block.oreRedstone);
		addBlockToMiningList(Block.oreRedstoneGlowing);
		addBlockToMiningList(Block.oreNetherQuartz);
		addBlockToMiningList(Block.oreLapis);
		addBlockToMiningList(Block.oreDiamond);
	}

	public static void addBlockToMiningList(Block block)
	{
		if (block != null)
		{
			Pair<Integer, Integer> ore = new Pair<Integer, Integer>(block.blockID, -1);
			if (!oreList.contains(ore))
			{
				oreList.add(ore);
			}
		}
	}

	public ZoneMining(HiveComplex complex, Vector3 start, Vector3 end)
	{
		super(complex, start, end);
	}

	public ZoneMining clearAll()
	{
		this.clearAll = true;
		return this;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (ticks % 10 == 0)
		{
			this.scan();
		}
	}

	public void scan()
	{
		Vector3 start = this.start;
		Vector3 end = this.end;
		this.scannedBlocks.clear();
		this.scannedSortedPositions.clear();

		for (int x = (int) start.x; x < (int) end.x; x++)
		{
			for (int y = (int) start.y; y < (int) end.y; y++)
			{
				for (int z = (int) start.z; z < (int) end.z; z++)
				{
					int blockID = this.world.getBlockId(x, y, z);
					Block block = Block.blocksList[blockID];

					if (block != null && this.canMine(blockID, this.world.getBlockMetadata(x, y, z)))
					{
						Vector3 position = new Vector3(x, y, z);

						HashSet<Vector3> vecs = this.scannedSortedPositions.get(block);

						if (vecs == null)
						{
							vecs = new HashSet<Vector3>();
						}

						this.scannedBlocks.add(position);
						vecs.add(position);

						this.scannedSortedPositions.put(block, vecs);
					}
				}
			}
		}
	}

	@Override
	public boolean canAssignDrone(IArtillect drone)
	{
		return drone instanceof EntityWorker;
	}

	public boolean canMine(int id, int meta)
	{
		return this.clearAll || ZoneMining.oreList.contains(new Pair<Integer, Integer>(id, -1)) || ZoneMining.oreList.contains(new Pair<Integer, Integer>(id, meta));
	}
}
