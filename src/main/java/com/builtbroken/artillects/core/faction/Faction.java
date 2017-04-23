package com.builtbroken.artillects.core.faction;

import com.builtbroken.artillects.api.IFaction;
import com.builtbroken.artillects.api.IFactionMember;
import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.framework.access.AccessProfile;
import com.builtbroken.mc.framework.access.AccessUser;
import com.builtbroken.mc.framework.access.IProfileContainer;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;

/**
 * Data object for a faction and all of its settings. Does not track land claimed by
 * a faction, use {@link FactionMap} to check claim data and land holdings. Maps can be
 * accessed from {@link FactionManager} using various methods.
 * <p/>
 * Use {@link FactionManager} to generate new factions so data can be tracked correctly.
 *
 * @author Darkguardsman
 */
public class Faction implements IFaction, IProfileContainer, IVirtualObject
{
    //TODO do check if needs saved, so it not saving every tick

    /** Top most permission system used by the faction */
    private AccessProfile globalProfile;
    /** Display name of the faction */
    private String name;
    /** Unique ID used to reference the faction */
    private String id;

    private Faction(String name)
    {
        this.name = name;
    }

    private Faction(NBTTagCompound tag)
    {
        this.load(tag);
    }

    public static Faction loadFaction(NBTTagCompound tag)
    {
        return new Faction(tag);
    }

    public static Faction newFaction(String name)
    {
        return new Faction(name).setID("World_" + System.currentTimeMillis());
    }

    public static Faction newFaction(String name, String id)
    {
        return new Faction(name).setID(id);
    }

    public static Faction newFaction(EntityPlayer player, String name)
    {
        Faction faction = new Faction(name).setID(name);
        faction.getAccessProfile().getOwnerGroup().addMember(player);
        return faction;
    }

    @Override
    public AccessProfile getAccessProfile()
    {
        return globalProfile;
    }

    @Override
    public void setAccessProfile(AccessProfile profile)
    {
        globalProfile = profile;
    }

    @Override
    public boolean canAccess(String username)
    {
        if (getAccessProfile() != null)
        {
            return this.getAccessProfile().getUserAccess(username).getGroup() != null;
        }
        return false;
    }

    @Override
    public boolean hasNode(EntityPlayer player, String node)
    {
        if (getAccessProfile() != null)
        {
            AccessUser user = getAccessProfile().getUserAccess(player);
            return user != null ? user.hasNode(node) : false;
        }
        return false;
    }

    @Override
    public boolean hasNode(String username, String node)
    {
        if (getAccessProfile() != null)
        {
            AccessUser user = getAccessProfile().getUserAccess(username);
            return user != null ? user.hasNode(node) : false;
        }
        return false;
    }

    @Override
    public void onProfileChange()
    {
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "bbm/artillects/factions/Faction_" + this.name  + ".dat");
    }

    @Override
    public void setSaveFile(File file)
    {
    }

    @Override
    public boolean shouldSaveForWorld(World world)
    {
        return world != null && world.provider.dimensionId == 0;
    }

    @Override
    public String getID()
    {
        return id;
    }


    protected Faction setID(String id)
    {
        this.id = id;
        return this;
    }

    /** Display name of the faction */
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean isMember(Object obj)
    {
        Faction faction = null;
        if (obj instanceof IFactionMember)
        {
            faction = ((IFactionMember) obj).getFaction();
        }
        else if (obj instanceof Entity)
        {
            faction = FactionManager.getFaction((Entity) obj);
        }
        return faction != null && faction.getID() == getID();
    }

}
