package artillects.core.region;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import resonant.lib.access.AccessUser;
import universalelectricity.api.vector.Vector2;
import artillects.core.FactionPerms;
import artillects.core.interfaces.IFaction;

/** Manager of all land creation, destruction, and claiming
 * 
 * @author Darkguardsman */
public class LandManager
{
    private World world;
    private IFaction faction;

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
            AccessUser user = faction.getAccessProfile().getUserAccess(player.username);
            if (user != null && user.getGroup() != null)
            {
                if (user.hasNode(FactionPerms.CLAIM.node()))
                {
                    List<String> log = new LinkedList<String>();
                    boolean claimed = claim(log, start, end);
                    for (String string : log)
                    {
                        if (string.startsWith("out:"))
                            player.sendChatToPlayer(ChatMessageComponent.createFromText(string.replace("out:", "")));
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
        return false;
    }
}
