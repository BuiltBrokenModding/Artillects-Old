package com.builtbroken.artillects.core.faction.land;

import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;

/**
 * Large region of chunks claimed by a faction. Land contains everything that a faction owns. This may include villages, towns, roads, forts, settlements, outposts, etc.
 * <p/>
 * Can be thought of as a county, inside of a state, inside of a country
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class Land extends Region implements IVirtualObject
{
    private String name;

    private Land(String name)
    {
        super(name);
    }

    private Land(NBTTagCompound tag)
    {
        super(null);
        load(tag);
        if (name == null)
        {
            name = "Land_" + System.currentTimeMillis();
        }
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "bbm/artillects/land/Land_" + this.name);
    }

    @Override
    public void setSaveFile(File file)
    {

    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return false;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        return null;
    }
}
