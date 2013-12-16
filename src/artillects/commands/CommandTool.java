package artillects.commands;

import java.io.File;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import artillects.VectorWorld;
import artillects.hive.HiveComplexManager;
import artillects.hive.HiveComplex;
import artillects.hive.schematics.NBTFileHandler;

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
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection load - creates a schematic using the world selection"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection load <name> - loads a schematic from file"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection save [name] - saves the loaded schematic"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection build pointOne - build a schematic at first selection point"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection build pointTwo - build a schematic at second selection point"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool slection build x y z - builds a schematic at the location"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool hive ls - gets the number of active hives"));
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool hive get - gets the closest hive"));
                }
                else if (args.length >= 1 && args[0].equalsIgnoreCase("selection"))
                {
                    if (args.length >= 2 && args[1].equalsIgnoreCase("save"))
                    {
                        PlayerSelectionHandler.saveWorldSelection(player, args.length >= 3 ? args[2] : null);
                    }
                    else if (args.length >= 2 && args[1].equalsIgnoreCase("load"))
                    {
                        if (args.length >= 3)
                        {
                            File file = new File(NBTFileHandler.getBaseFolder(), "schematics/" + args[2] + ".dat");
                            if (file.exists())
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText("Loading " + args[2] + " from file"));
                                PlayerSelectionHandler.getSchematic(player, file);
                            }
                            else
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText("File not found! .mincraft/schematics/" + args[2] + ".dat"));
                            }
                        }
                        else
                        {
                            PlayerSelectionHandler.loadWorldSelection(player);
                        }
                    }
                    else if (args.length >= 2 && args[1].equalsIgnoreCase("build"))
                    {
                        if (PlayerSelectionHandler.hasSchematicLoaded(player))
                        {
                            if (args.length >= 3 && args[2].equalsIgnoreCase("pointOne"))
                            {
                                if (PlayerSelectionHandler.hasPointOne(player))
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Building schematic at point one"));
                                    PlayerSelectionHandler.getSchematic(player).build(PlayerSelectionHandler.getPointOne(player), false);
                                }
                                else
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Can't build without a location"));
                                }
                            }
                            else if (args.length >= 3 && args[2].equalsIgnoreCase("pointTwo"))
                            {
                                if (PlayerSelectionHandler.hasPointTwo(player))
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Building schematic at point two"));
                                    PlayerSelectionHandler.getSchematic(player).build(PlayerSelectionHandler.getPointTwo(player), false);
                                }
                                else
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Can't build without a location"));
                                }
                            }
                            else if (args.length >= 5)
                            {
                                try
                                {
                                    int x = Integer.parseInt(args[2]);
                                    int y = Integer.parseInt(args[3]);
                                    int z = Integer.parseInt(args[4]);
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Building schematic at " + x + "x " + y + "y " + z + "z "));
                                    PlayerSelectionHandler.getSchematic(player).build(new VectorWorld(player.worldObj, x, y, z), false);
                                }
                                catch (IllegalArgumentException e)
                                {
                                    player.sendChatToPlayer(ChatMessageComponent.createFromText("Can't build due to location parsing xyz"));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            player.sendChatToPlayer(ChatMessageComponent.createFromText("Can't build without a schematic loaded!"));
                        }
                    }
                    else
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool help"));
                    }
                }
                else if (args.length >= 1 && args[0].equalsIgnoreCase("hive"))
                {
                    if (args.length >= 2 && args[1].equalsIgnoreCase("ls"))
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromText("There are currently " + HiveComplexManager.instance().complexes.size() + " hive complexs loaded in the world."));
                    }
                    else if (args.length >= 2 && args[1].equalsIgnoreCase("get"))
                    {
                        HiveComplex complex = HiveComplexManager.instance().getClosestComplex(new VectorWorld(player), 10000);
                        if (complex != null)
                        {
                            player.sendChatToPlayer(ChatMessageComponent.createFromText("Closest within 10,000 blocks hive is at " + complex.location.toString()));
                        }
                        else
                        {
                            player.sendChatToPlayer(ChatMessageComponent.createFromText("No hive buildings found with in 10,000 blocks"));
                        }

                    }
                }
                else
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("/tool help"));
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
