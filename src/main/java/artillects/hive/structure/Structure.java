package artillects.hive.structure;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import artillects.hive.HiveEntityObject;

import com.builtbroken.common.Pair;

/** Entity that represents a structure peace in a hive complex
 * 
 * @author DarkGuardsman */
public class Structure extends HiveEntityObject
{
    public EnumStructurePeaces building;
    protected VectorWorld location;

    protected boolean isDamaged = false;

    HashMap<Vector3, ItemStack> missingBlocks = new HashMap<Vector3, ItemStack>();

    public Structure(EnumStructurePeaces building, VectorWorld location)
    {
        this.building = building;
        this.location = location;
    }

    public Structure(NBTTagCompound tag)
    {
        this.load(tag);
    }

    public Structure worldGen()
    {
        building.getSchematic().build(location, false);
        return this;
    }

    @Override
    public void init()
    {
        super.init();
        if (building == EnumStructurePeaces.PROCESSORROOM)
        {
            //new ZoneProcessing(complex, location.subtract(new Vector3(-8, 0, -8)), location.add(new Vector3(8, 5, 8)));
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        /** Tick is setup to make sure that only a few structures update per tick */
        if (this.ticks % (60 + location.x + location.y + location.z) == 0)
        {
            HashMap<Vector3, ItemStack> missingBlocks = new HashMap<Vector3, ItemStack>();
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
    public boolean addBlock(Vector3 location, ItemStack stack)
    {
        if (stack != null)
        {
            location.setBlock(this.location.world, stack.itemID, stack.getItemDamage());
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
    public void save(NBTTagCompound nbt)
    {
        nbt.setInteger("buildingID", building.ordinal());
        nbt.setCompoundTag("location", this.location.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new VectorWorld(nbt.getCompoundTag("location"));
        building = EnumStructurePeaces.values()[nbt.getInteger("buildingID")];
    }

    public boolean isDamaged()
    {
        return isDamaged;
    }

    public void loadBuildingRequest(HashMap<Vector3, Pair<ItemStack, Structure>> buildMap)
    {
        for (Entry<Vector3, ItemStack> entry : this.missingBlocks.entrySet())
        {
            buildMap.put(entry.getKey(), new Pair<ItemStack, Structure>(entry.getValue(), this));
        }
    }
}
