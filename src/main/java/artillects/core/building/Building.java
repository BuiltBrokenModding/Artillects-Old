package artillects.core.building;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import calclavia.lib.type.Pair;

/** Collection of structure peaces that forms a building. Essential in code this acts as a contain
 * for several structure peaces so its easier to manage larger HiveComplexs.
 * 
 * @author DarkGuardsman */
public class Building extends GhostObject
{
    private VectorWorld location;
    private final Set<BuildingPart> peaces = new LinkedHashSet<BuildingPart>();
    private boolean isDamaged = false;

    public Building(VectorWorld location)
    {
        this.location = location;
    }

    public Building worldGen()
    {
        if (peaces.size() > 0)
        {
            for (BuildingPart str : peaces)
            {
                str.worldGen();
            }
        }
        return this;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        Iterator<BuildingPart> structureIterator = peaces.iterator();
        while (structureIterator.hasNext())
        {
            this.isDamaged = false;
            BuildingPart structure = structureIterator.next();
            if (structure.isValid())
            {
                structure.updateEntity();
                if (structure.isDamaged)
                {
                    this.isDamaged = true;
                }
            }
            else
            {
                structureIterator.remove();
            }
        }
    }

    /** Is this building damage or in other words missing blocks */
    public boolean isDamaged()
    {
        return isDamaged;
    }

    /** Loads any peaces that this building request be placed. Calls to each str peace
     * 
     * @param buildMap - hashMap to load the block placement requests into */
    public void loadBuildingRequest(HashMap<Vector3, Pair<ItemStack, BuildingPart>> buildMap)
    {
        if (peaces.size() > 0)
        {
            for (BuildingPart str : peaces)
            {
                str.loadBuildingRequest(buildMap);
            }
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setCompoundTag("location", this.location.writeToNBT(new NBTTagCompound()));
        if (peaces.size() > 0)
        {
            NBTTagList list = new NBTTagList();
            for (BuildingPart str : peaces)
            {
                NBTTagCompound tag = new NBTTagCompound();
                str.save(tag);
                list.appendTag(tag);
            }
            nbt.setTag("Structures", list);
        }
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new VectorWorld(nbt.getCompoundTag("location"));
        NBTTagList nbttaglist = nbt.getTagList("Structures");
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound tag = (NBTTagCompound) nbttaglist.tagAt(i);
            BuildingPart structure = new BuildingPart(tag);
        }
    }
}
