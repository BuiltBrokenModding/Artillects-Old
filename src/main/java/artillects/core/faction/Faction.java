package artillects.core.faction;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import resonant.lib.access.AccessProfile;
import resonant.lib.access.IProfileContainer;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;

/** Faction is more of a container for all settings and data related to a faction.
 * 
 * @author Darkguardsman */
public class Faction implements IProfileContainer, IVirtualObject
{
    private AccessProfile accessProfile;
    private File saveFile;
    private String name;
    
    public Faction()
    {        
        saveFile = new File(NBTUtility.getSaveDirectory(), "artillects/factions/Faction_" + this.name);
        SaveManager.register(this);
    }

    @Override
    public AccessProfile getAccessProfile()
    {
        return accessProfile;
    }

    @Override
    public void setAccessProfile(AccessProfile profile)
    {
        accessProfile = profile;
    }

    @Override
    public boolean canAccess(String username)
    {
        if(getAccessProfile() != null)
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
        return this.saveFile;
    }

    @Override
    public void setSaveFile(File file)
    {        
    }

}
