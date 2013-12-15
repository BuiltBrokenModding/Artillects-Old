package artillects.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;

public class CommandTool extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "tool";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/tool help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        try
        {
            if (sender instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) sender;
                if (args == null || args.length == 0 || args[0].equalsIgnoreCase("help"))
                {

                }
                else if (args.length >= 2 && args[1].equalsIgnoreCase("selection"))
                {
                    if (args.length >= 3 && args[1].equalsIgnoreCase("save"))
                    {
                        if (PlayerSelectionHandler.hasSchematicLoaded(player))
                        {
                            PlayerSelectionHandler
                        }
                        else
                        {
                            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Saving requires a schematic to be loaded into memory"));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "help", "selection load <name>", "selection save [name]", "selection scan", "zone new <name>" }) : null;
    }

}
