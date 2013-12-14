package artillects.hive;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import artillects.Vector3;

public class ZoneMining extends Zone
{
    public ZoneMining(World world, Vector3 start, Vector3 end)
    {
        super(world, start, end);
    }

    public void calculate()
    {
        Vector3 start = this.zone.start;
        Vector3 end = this.zone.end;

        for (int x = (int) start.x; x < (int) end.x; x++)
        {
            for (int y = (int) start.y; x < (int) end.y; x++)
            {
                for (int z = (int) start.z; z < (int) end.z; z++)
                {
                    int blockID = this.world.getBlockId(x, y, z);
                    Block block = Block.blocksList[blockID];

                    if (block != null && this.scannedSortedPositions.containsKey(block))
                    {
                        this.scannedPositions.add(new Vector3(x, y, z));
                        this.scannedSortedPositions.get(block).add(new Vector3(x, y, z));
                    }
                }
            }
        }
    }
}
