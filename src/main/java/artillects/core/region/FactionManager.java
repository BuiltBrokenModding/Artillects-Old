package artillects.core.region;

import artillects.core.interfaces.IFactionMember;
import com.builtbroken.jlib.data.IPos2D;
import com.builtbroken.jlib.data.IPos3D;
import com.builtbroken.mc.api.IPosWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;

/** Manager that handles everything related to faction control, setup, and interaction
 * 
 * @author Darkguardsman */
public class FactionManager
{
    private static HashMap<Integer, Faction> factions = new HashMap<Integer, Faction>();
    private static HashMap<String, Integer> playerToFaction = new HashMap<String, Integer>();
    private static Faction neutralFaction;

    static
    {
        neutralFaction = new Faction();
        neutralFaction.setID(0);
        neutralFaction.setName("Neutral");
        addFaction(neutralFaction);
    }

    public static Faction getFaction(int id)
    {
        return factions.get(id);
    }

    public static Faction getFaction(Entity entity)
    {
        if (entity instanceof IFactionMember)
        {
            return ((IFactionMember) entity).getFaction();
        }
        if (entity instanceof EntityPlayer)
        {
            if (playerToFaction.containsKey(((EntityPlayer) entity).getCommandSenderName()))
            {
                int id = playerToFaction.get(((EntityPlayer) entity).getCommandSenderName());
                if(factions.containsKey(id))
                    return factions.get(id);
            }
            playerToFaction.put(((EntityPlayer) entity).getCommandSenderName(), 0);
        }
        return neutralFaction;
    }

    public static Faction getFaction(IPosWorld vec)
    {
        return getFaction(vec.world(), vec);
    }

    public static Faction getFaction(World world, IPos3D vec)
    {
        return getFaction(world, (IPos2D) vec);
    }

    public static Faction getFaction(World world, IPos2D vec)
    {
        for (Faction faction : factions.values())
        {
            if (faction.controls(world, vec))
            {
                return faction;
            }
        }
        return neutralFaction;
    }

    public static void addFaction(Faction faction)
    {
        if (getFaction(faction.getID()) == null)
        {
            factions.put(faction.getID(), faction);
        }
    }

    public static Faction create(EntityPlayer player, String name)
    {
        return create(name);
    }

    public static Faction create(String name)
    {
        Faction faction = new Faction(name);
        addFaction(faction);
        return faction;
    }
}
