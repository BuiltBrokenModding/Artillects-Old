package artillects.core.building;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map.Entry;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class BuildingPart extends GhostObject
{
    public EnumStructurePeaces building;
    protected Location location;

    protected boolean isDamaged = false;

    HashMap<Pos, ItemStack> missingBlocks = new HashMap<Pos, ItemStack>();

    public BuildingPart(EnumStructurePeaces building, Location location)
    {
        this.building = building;
        this.location = location;
    }

    public BuildingPart(NBTTagCompound tag)
    {
        this.load(tag);
    }

    public BuildingPart worldGen()
    {
        building.getSchematic().build(location, false);
        return this;
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        /** Tick is setup to make sure that only a few structures update per tick */
        if (this.ticks % (60 + location.x() + location.y() + location.z()) == 0)
        {
            HashMap<Pos, ItemStack> missingBlocks = new HashMap<Pos, ItemStack>();
            building.getSchematic().getBlocksToPlace(this.location, missingBlocks, true, true);
            if (!missingBlocks.isEmpty())
            {
                System.out.println(this.toString() + " found damage");
                this.missingBlocks.putAll(missingBlocks);
                this.isDamaged = true;
            }
            else
            {
                this.isDamaged = false;
            }
        }
    }

    /** Called by a fabricator drone to place a block into the structure. This is mainly used to
     * clear out the buildPosition map. This way the structure can become undamaged before an update
     * scan is needed
     * 
     * @param location - placed location
     * @param stack - stack to place, stack size is ignored
     * @return */
    public boolean addBlock(Pos location, ItemStack stack)
    {
        if (stack != null)
        {
            location.setBlock(this.location.world(), Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
            if (this.missingBlocks.containsKey(location))
            {
                if (this.missingBlocks.get(location) != null)
                {
                    if (this.missingBlocks.get(location).isItemEqual(stack))
                    {
                        this.missingBlocks.remove(location);
                        return true;
                    }
                }
                else
                {
                    this.missingBlocks.remove(location);
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setInteger("buildingID", building.ordinal());
        nbt.setTag("location", this.location.writeNBT(new NBTTagCompound()));
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new Location(nbt.getCompoundTag("location"));
        building = EnumStructurePeaces.values()[nbt.getInteger("buildingID")];
    }

    public boolean isDamaged()
    {
        return isDamaged;
    }

    public void loadBuildingRequest(HashMap<Pos, Pair<ItemStack, BuildingPart>> buildMap)
    {
        for (Entry<Pos, ItemStack> entry : this.missingBlocks.entrySet())
        {
            buildMap.put(entry.getKey(), new Pair<ItemStack, BuildingPart>(entry.getValue(), this));
        }
    }
}
