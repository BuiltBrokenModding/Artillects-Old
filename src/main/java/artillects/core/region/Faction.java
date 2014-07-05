package artillects.core.region;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import resonant.lib.access.AccessProfile;
import resonant.lib.access.IProfileContainer;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import artillects.core.interfaces.IID;

/** Faction is more of a container for all settings and data related to a faction.
 * 
 * @author Darkguardsman */
public class Faction implements IProfileContainer, IVirtualObject, IID<Integer, Faction>
{
    //vars
    private AccessProfile globalProfile;
    private HashMap<World, AccessProfile> worldAccessProfiles = new HashMap<World, AccessProfile>();
    private String name;
    private int id;
    private static int nextID = 0;

    public Faction()
    {

    }

    public Faction(String name)
    {
        this.name = name;
        this.id = nextID++;
    } 
    
    public Faction(NBTTagCompound tag)
    {
        this.load(tag);
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
    public void onProfileChange()
    {
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "artillects/factions/Faction_" + this.name);
    }

    @Override
    public void setSaveFile(File file)
    {
    }

    @Override
    public Integer getID()
    {
        return id;
    }

    @Override
    public Faction setID(Integer id)
    {
        this.id = id;
        return this;
    }

}
