package artillects.core.zone;


import artillects.core.building.BuildingPart;
import artillects.drone.HiveComplex;
import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.api.edit.IWorldEdit;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Zone designed for drone to build a construct
 * 
 * @author Darkguardsman */
public class ZoneBuilding extends Zone
{
    HiveComplex complex;

    public HashMap<Pos, Pair<ItemStack, BuildingPart>> buildPosition = new HashMap<Pos, Pair<ItemStack, BuildingPart>>();

    public ZoneBuilding(HiveComplex complex, Pos start, Pos end)
    {
        super(complex.location.world(), start, end);
        this.complex = complex;
    }

    public ZoneBuilding(HiveComplex hiveComplex, int i)
    {
        this(hiveComplex, new Pos(hiveComplex.location.x() + i, hiveComplex.location.y() + i, hiveComplex.location.z() + i) , new Pos(hiveComplex.location.x() - i, hiveComplex.location.y() - i, hiveComplex.location.z() - i));
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
    public IWorldEdit getClosestBlock(Location vec)
    {
        List<IWorldEdit> list = getClosestBlocks(vec, 1);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * Gets the closest n count of blocks to the location
     * @param vec - location to search from
     * @param num - n count
     * @return list of blocks, never null but can be empty
     */
    public List<IWorldEdit> getClosestBlocks(Location vec, int num)
    {
        List<IWorldEdit> blocks = new ArrayList();
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
    public boolean placeBlock(Pos location, ItemStack stack)
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
