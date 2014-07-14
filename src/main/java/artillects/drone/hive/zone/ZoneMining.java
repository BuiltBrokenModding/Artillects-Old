package artillects.drone.hive.zone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import resonant.lib.type.Pair;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.utility.AreaScanner;
import artillects.core.utility.IBlockSelector;
import artillects.drone.entity.IArtillect;
import artillects.drone.entity.workers.EntityWorker;

public class ZoneMining extends Zone implements IBlockSelector
{
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

    public ZoneMining(World world, Vector3 start, Vector3 end)
    {
        super(world, start, end);
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
        if (ticks % 20 == 0)
        {
            this.scannedBlocks.clear();
            AreaScanner.scanArea(this.world, this, this.start, this.end);
        }
    }

    @Override
    public void onScan(VectorWorld loc)
    {
        if (loc != null && loc.world == this.world)
        {
            int blockID = loc.getBlockID();
            Block block = Block.blocksList[blockID];

            if (block != null && this.canMine(blockID, loc.getBlockMetadata()))
            {
                this.scannedBlocks.add(new Vector3(loc.x, loc.y, loc.z));
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
