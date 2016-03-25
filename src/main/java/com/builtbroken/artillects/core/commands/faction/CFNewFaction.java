package com.builtbroken.artillects.core.commands.faction;

import com.builtbroken.mc.core.commands.ext.SubCommandWithName;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class CFNewFaction extends SubCommandWithName
{
    public CFNewFaction()
    {
        super("faction");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String factionName, String[] args)
    {
        return false;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String factionName, String[] args)
    {
        return false;
    }
}
