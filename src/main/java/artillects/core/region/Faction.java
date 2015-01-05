package artillects.core.region;

import artillects.core.interfaces.IFaction;
import artillects.core.interfaces.IFactionMember;
import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.lib.access.AccessProfile;
import com.builtbroken.mc.lib.access.IProfileContainer;
import com.builtbroken.mc.lib.helper.nbt.NBTUtility;
import com.builtbroken.mc.lib.transform.vector.IVector2;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.File;
import java.util.HashMap;

/** Faction is more of a container for all settings and data related to a faction.
 * 
 * @author Darkguardsman */
public class Faction implements IFaction, IProfileContainer, IVirtualObject
{
    //vars
    private AccessProfile globalProfile;
    private HashMap<World, AccessProfile> worldAccessProfiles = new HashMap<World, AccessProfile>();
    private HashMap<World, LandManager> worldLandManagers = new HashMap<World, LandManager>();
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

    /** Display name of the faction */
    public void setName(String name)
    {
        this.name = name;        
    }

    /** Does this faction control this land */
    public boolean controls(World world, IVector2 vec)
    {
        if(worldLandManagers.containsKey(world) && worldLandManagers.get(world) != null)
        {
            worldLandManagers.get(world).controls(vec);
        }
        return false;
    }

    @Override
    public boolean isMember(Object obj)
    {
        Faction faction = null;
        if (obj instanceof IFactionMember)
            faction = ((IFactionMember) obj).getFaction();
        else if (obj instanceof Entity)
            faction = FactionManager.getFaction((Entity) obj);
        if (faction != null && faction.getID() == getID())
            return true;
        return false;
    }

}
