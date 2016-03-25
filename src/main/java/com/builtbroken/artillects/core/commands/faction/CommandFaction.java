package com.builtbroken.artillects.core.commands.faction;

import com.builtbroken.mc.prefab.commands.ModularCommand;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class CommandFaction extends ModularCommand
{
    public static final CommandFaction INSTANCE = new CommandFaction();
    private ModularCommand sub_command_new;

    public CommandFaction()
    {
        super("faction");
        sub_command_new = addCommand(new CFNew());
    }
}
