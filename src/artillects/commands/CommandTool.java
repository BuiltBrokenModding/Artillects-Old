package artillects.commands;

import java.util.List;

import artillects.hive.Hive;

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
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("slection load <name>"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("slection save [name]"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("slection scan"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("zone ls"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("zone new <name>"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("zone remove <name>"));
                }
                else if (args.length >= 1 && args[0].equalsIgnoreCase("selection"))
                {
                    if (args.length >= 2 && args[1].equalsIgnoreCase("save"))
                    {
                        PlayerSelectionHandler.saveWorldSelection(player, args.length >= 3 ? args[2] : null);
                    }
                }
                else if (args.length >= 1 && args[0].equalsIgnoreCase("zone"))
                {
                    if (args.length >= 2 && args[1].equalsIgnoreCase("ls"))
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromText("There are currently " + Hive.instance().activeZones.size() + " zones part of the hive."));
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
