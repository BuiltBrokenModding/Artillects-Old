package artillects.core.zone;


import artillects.core.building.BuildingBlock;
import artillects.core.building.BuildingPart;
import artillects.drone.HiveComplex;
import net.minecraft.item.ItemStack;
import resonant.lib.type.Pair;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Zone designed for drone to build a construct
 * 
 * @author Darkguardsman */
public class ZoneBuilding extends Zone
{
    HiveComplex complex;

    public HashMap<Vector3, Pair<ItemStack, BuildingPart>> buildPosition = new HashMap<Vector3, Pair<ItemStack, BuildingPart>>();

    public ZoneBuilding(HiveComplex complex, Vector3 start, Vector3 end)
    {
        super(complex.location.world(), start, end);
        this.complex = complex;
    }

    public ZoneBuilding(HiveComplex hiveComplex, int i)
    {
        this(hiveComplex, hiveComplex.location.clone().add(i), hiveComplex.location.clone().add(-i));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (ticks % 20 == 0 && complex != null)
        {
            this.buildPosition.clear();
            for (BuildingPart str : complex.damagedPeaces)
            {
                str.loadBuildingRequest(this.buildPosition);
            }
        }
    }

    /**
     * Gets the first closest block for building
     * @param vec - location to search from
     * @return BuildBlock instance containing the placement stack and location to place the block
     */
    public BuildingBlock getClosestBlock(VectorWorld vec)
    {
        List<BuildingBlock> list = getClosestBlocks(vec, 1);
        return list.get(0);
    }

    /**
     * Gets the closest n count of blocks to the location
     * @param vec - location to search from
     * @param num - n count
     * @return list of blocks, never null but can be empty
     */
    public List<BuildingBlock> getClosestBlocks(VectorWorld vec, int num)
    {
        List<BuildingBlock> blocks = new ArrayList();
        if (!buildPosition.isEmpty() && vec.world() == this.world)
        {
                //TODO sort collection of all build positions by distance to vec
                //TODO then return first n count of positions
        }
        return blocks;
    }

    /** Called by a builder to place a block.
     * 
     * @param location - location in the zone's world
     * @param stack - item stack, stack size is ignored
     * @return true if the block was placed */
    public boolean placeBlock(Vector3 location, ItemStack stack)
    {
        if (this.buildPosition.get(location) != null)
        {
            if (this.buildPosition.get(location).right() != null)
            {
                if (this.buildPosition.get(location).right().addBlock(location, stack))
                {
                    this.buildPosition.remove(location);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean doesZoneNeedWorkers()
    {
        return assignedArtillects.size() < 3 && !this.buildPosition.isEmpty();
    }
}
