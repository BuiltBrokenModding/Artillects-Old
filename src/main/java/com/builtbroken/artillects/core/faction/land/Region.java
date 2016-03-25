package com.builtbroken.artillects.core.faction.land;

import com.builtbroken.artillects.core.faction.Faction;
import com.builtbroken.artillects.core.faction.FactionManager;
import com.builtbroken.mc.api.ISave;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public abstract class Region implements ISave
{
    /** Faction who owns this region */
    protected String faction;
    protected String name;

    protected Region(String name)
    {
        this.setName(name);
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        if (nbt.hasKey("name"))
        {
            this.setName(nbt.getString("name"));
        }
        if (nbt.hasKey("faction"))
        {
            this.faction = nbt.getString("faction");
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        if (name != null && !name.isEmpty())
        {
            nbt.setString("name", name);
        }
        if (faction != null && !faction.isEmpty())
        {
            nbt.setString("faction", faction);
        }
        return nbt;
    }

    public void setFaction(String faction)
    {
        this.faction = faction;
    }

    public Faction getFaction()
    {
        return FactionManager.getFaction(faction);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
