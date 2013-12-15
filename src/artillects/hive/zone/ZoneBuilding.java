package artillects.hive.zone;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Vector3;

public class ZoneBuilding extends Zone
{
	public final HashMap<Vector3, ItemStack> buildPosition = new HashMap<Vector3, ItemStack>();

	/**
	 * The location in which a chest with resource is provided.
	 */
	public Vector3 resourceLocation;

	public ZoneBuilding(World world, Vector3 start, Vector3 end)
	{
		super(world, start, end);
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

		this.buildPosition.clear();

		for (int x = (int) start.x; x < (int) end.x; x++)
		{
			for (int y = (int) start.y; y < (int) end.y; y++)
			{
				for (int z = (int) start.z; z < (int) end.z; z++)
				{
					int blockID = this.world.getBlockId(x, y, z);
					Block block = Block.blocksList[blockID];

					if (block != null && this.canMine(block))
					{
						Vector3 position = new Vector3(x, y, z);
						// TODO: Load schematic here.
					}
				}
			}
		}
	}

	public boolean canMine(Block block)
	{
		return block.blockID == Block.oreIron.blockID;
	}
}
