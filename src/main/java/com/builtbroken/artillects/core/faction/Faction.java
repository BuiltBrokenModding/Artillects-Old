package com.builtbroken.artillects.core.faction;

import com.builtbroken.artillects.core.interfaces.IFaction;
import com.builtbroken.artillects.core.interfaces.IFactionMember;
import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.lib.access.AccessProfile;
import com.builtbroken.mc.lib.access.AccessUser;
import com.builtbroken.mc.lib.access.IProfileContainer;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;

/**
 * Faction is more of a container for all settings and data related to a faction.
 *
 * @author Darkguardsman
 */
public class Faction implements IFaction, IProfileContainer, IVirtualObject
{
    //vars
    private AccessProfile globalProfile;
    //private HashMap<World, LandManager> worldLandManagers = new HashMap<World, LandManager>();
    private String name;
    private String id;

    private Faction()
    {

    }

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
        Faction faction = new Faction(name).setID("player_" + System.currentTimeMillis());
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
        return new File(NBTUtility.getSaveDirectory(), "bbm/artillects/factions/Faction_" + this.name);
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
