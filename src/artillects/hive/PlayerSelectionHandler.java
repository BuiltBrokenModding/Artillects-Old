package artillects.hive;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import artillects.VectorWorld;
import artillects.hive.schematics.Schematic;

public class PlayerSelectionHandler
{
    private static HashMap<String, VectorWorld[]> playerPointSelection = new HashMap<String, VectorWorld[]>();
    private static HashMap<String, Schematic> playerSchematic = new HashMap<String, Schematic>();

    public static void setPointOne(EntityPlayer player, VectorWorld point)
    {
        if (player != null)
        {
            VectorWorld b = null;
            if (playerPointSelection.get(player.username) != null)
                b = playerPointSelection.get(player.username)[1];

            playerPointSelection.put(player.username, new VectorWorld[] { point, b });
            if (point != null)
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Pos one set to " + point.toString()));
        }
    }

    public static boolean hasPointOne(EntityPlayer player)
    {
        return playerPointSelection.get(player.username) != null && playerPointSelection.get(player.username)[0] != null;
    }

    public static void setPointTwo(EntityPlayer player, VectorWorld point)
    {
        if (player != null)
        {
            VectorWorld a = null;
            if (playerPointSelection.get(player.username) != null)
                a = playerPointSelection.get(player.username)[0];

            playerPointSelection.put(player.username, new VectorWorld[] { a, point });

            if (point != null)
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Pos two set to " + point.toString()));
        }
    }

    public static boolean hasPointTwo(EntityPlayer player)
    {
        return playerPointSelection.get(player.username) != null && playerPointSelection.get(player.username)[1] != null;
    }

    public static boolean hasBothPoints(EntityPlayer player)
    {
        return hasPointOne(player) && hasPointTwo(player);
    }

    public void loadWorldSelection(EntityPlayer player, String name)
    {
        if (hasBothPoints(player))
        {
            VectorWorld pointOne = PlayerSelectionHandler.playerPointSelection.get(player.username)[0];
            VectorWorld pointTwo = PlayerSelectionHandler.playerPointSelection.get(player.username)[1];
            if (pointOne.world == pointTwo.world)
            {
                if (name == null)
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh.mm.ss");
                    name = player.username + "_Schematic_" + dateFormat.format(new Date());

                }
                Schematic schematic = new Schematic().loadWorldSelection(pointOne.world, pointOne, pointTwo);
                playerSchematic.put(player.username, schematic);

                player.sendChatToPlayer(ChatMessageComponent.createFromText("Loaded selection into memory"));
            }
            else
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Selection points must be in the same world"));
            }
        }
    }
}
