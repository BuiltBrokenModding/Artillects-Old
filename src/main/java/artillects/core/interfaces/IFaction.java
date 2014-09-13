package artillects.core.interfaces;

import resonant.lib.access.java.IProfileContainer;
import resonant.lib.utility.nbt.IVirtualObject;

/** Interface applied to objects that will become a faction
 * 
 * @author Darkgardsman */
public interface IFaction extends IVirtualObject, IProfileContainer, IID<Integer, IFaction>
{
    /** Checks if the object is a memember of the faction */
    public boolean isMember(Object obj);
}
