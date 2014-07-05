package artillects.core.interfaces;

import artillects.core.region.Faction;
import resonant.lib.access.IProfileContainer;
import resonant.lib.utility.nbt.IVirtualObject;
import universalelectricity.api.net.IUpdate;

/** Interface applied to objects that will become a faction
 * 
 * @author Darkgardsman */
public interface IFaction extends IVirtualObject, IProfileContainer, IID<Integer, IFaction>
{
    /** Checks if the object is a memember of the faction */
    public boolean isMember(Object obj);
}
