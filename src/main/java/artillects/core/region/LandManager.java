package artillects.core.region;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import artillects.core.FactionPerms;
import artillects.core.interfaces.IFaction;
import resonant.lib.access.java.AccessUser;
import universalelectricity.core.transform.vector.IVector2;
import universalelectricity.core.transform.vector.Vector2;

/** Manager of all land creation, destruction, and claiming
 * 
 * @author Darkguardsman */
public class LandManager
{
    private World world;
    private IFaction faction;
    private int nextLandID = 0;
    private HashMap<Integer, Land> controlledLand = new LinkedHashMap<Integer, Land>();

    public LandManager(IFaction faction, World world)
    {
        this.world = world;
        this.faction = faction;
    }

    /** Claims an area for the faction
     * 
     * @param player - player claiming the area
     * @param start - starting point
     * @param end - ending point
     * @return true if it was claimed */
    public boolean claim(EntityPlayer player, Vector2 start, Vector2 end)
    {
        if (faction != null)
        {
            AccessUser user = faction.getAccessProfile().getUserAccess(player);
            if (user != null && user.getGroup() != null)
            {
                if (user.hasNode(FactionPerms.CLAIM.node()))
                {
                    List<String> log = new LinkedList<String>();
                    boolean claimed = claim(log, start, end);
                    for (String string : log)
                    {
                        if (string.startsWith("out:"))
                            player.addChatComponentMessage(new ChatComponentText(string.replace("out:", "")));
                    }
                    return claimed;
                }
            }
        }
        return false;
    }

    /** Claims an area for the faction
     * 
     * @param log - collection of errors or info
     * @param start - starting point
     * @param end - ending point
     * @return true if it was claimed */
    public boolean claim(List<String> log, Vector2 start, Vector2 end)
    {
        if (world != null)
        {

        }
        else
        {
            log.add("Fail:World is null");
        }
        return false;
    }

    /** Does this manager contain this point as part of one of its land areas */
    public boolean controls(IVector2 vec)
    {
        if (world != null && !controlledLand.isEmpty())
        {
            for (Land land : controlledLand.values())
            {
                if (land.controls(vec))
                    return true;
            }
        }
        return false;
    }
}
