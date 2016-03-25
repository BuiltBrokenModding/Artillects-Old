package com.builtbroken.artillects.core.faction.land;

import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.core.handler.SaveManager;
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
    private String id;

    private Land(World world, String name)
    {
        super(world, name);
        id = "" + System.nanoTime(); //TODO Need a better id system
        SaveManager.register(this);
    }

    private Land(NBTTagCompound tag)
    {
        super(null, null);
        load(tag);
        if (name == null)
        {
            name = "Land_" + System.currentTimeMillis();
        }
        if (id == null)
        {
            id = "" + System.nanoTime();//TODO Need a better id system
        }
        SaveManager.register(this);
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "bbm/artillects/land/Land_" + this.id);
    }

    @Override
    public void setSaveFile(File file)
    {

    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return world == this.world;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        super.load(nbt);
        if (nbt.hasKey("id"))
        {
            id = nbt.getString("id");
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        super.save(nbt);
        if (id != null && !id.isEmpty())
        {
            nbt.setString("id", id);
        }
        return nbt;
    }
}
