package artillects.core.interfaces;

import resonant.lib.access.IProfileContainer;
import resonant.lib.utility.nbt.IVirtualObject;
import universalelectricity.api.net.IUpdate;

/** Interface applied to objects that will become a faction
 * 
 * @author Darkgardsman */
public interface IFaction extends IVirtualObject, IUpdate, IProfileContainer
{
    public String getID();
    
    /** Checks if the object is a memember of the faction */
    public boolean isMember(Object obj);
}
