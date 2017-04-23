package com.builtbroken.artillects.core.building;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.imp.transform.vector.Location;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/** Collection of structure peaces that forms a building. Essential in code this acts as a contain
 * for several structure peaces so its easier to manage larger HiveComplexs.
 *
 * @author DarkGuardsman */
public class Building extends GhostObject
{
    private Location location;
    private final Set<BuildingPart> peaces = new LinkedHashSet<BuildingPart>();
    private boolean isDamaged = false;

    public Building(Location location)
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
    public void loadBuildingRequest(HashMap<Pos, Pair<ItemStack, BuildingPart>> buildMap)
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
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setTag("location", this.location.writeNBT(new NBTTagCompound()));
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
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new Location(nbt.getCompoundTag("location"));
        NBTTagList nbttaglist = nbt.getTagList("Structures", 0);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound tag = nbttaglist.getCompoundTagAt(i);
            BuildingPart structure = new BuildingPart(tag);
        }
    }
}
