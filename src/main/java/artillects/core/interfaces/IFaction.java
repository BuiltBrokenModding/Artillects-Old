package artillects.core.interfaces;


import com.builtbroken.mc.api.IVirtualObject;
import com.builtbroken.mc.lib.access.IProfileContainer;

/** Interface applied to objects that will become a faction
 * 
 * @author Darkgardsman */
public interface IFaction extends IVirtualObject, IProfileContainer, IID<Integer, IFaction>
{
    /** Checks if the object is a memember of the faction */
    public boolean isMember(Object obj);
}
