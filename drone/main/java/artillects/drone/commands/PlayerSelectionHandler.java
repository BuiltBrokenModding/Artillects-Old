package artillects.drone.commands;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ChatMessageComponent;
import resonant.lib.schematic.SchematicMap;
import universalelectricity.api.vector.VectorWorld;

public class PlayerSelectionHandler
{
	private static HashMap<String, VectorWorld[]> playerPointSelection = new HashMap<String, VectorWorld[]>();
	private static HashMap<String, SchematicMap> playerSchematic = new HashMap<String, SchematicMap>();

	public static VectorWorld getPointOne(EntityPlayer player)
	{
		return playerPointSelection.get(player.username) != null ? playerPointSelection.get(player.username)[0] : null;
	}

	public static VectorWorld getPointTwo(EntityPlayer player)
	{
		return playerPointSelection.get(player.username) != null ? playerPointSelection.get(player.username)[1] : null;
	}

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

	public static void loadWorldSelection(EntityPlayer player)
	{
		if (hasBothPoints(player))
		{
			VectorWorld pointOne = PlayerSelectionHandler.playerPointSelection.get(player.username)[0];
			VectorWorld pointTwo = PlayerSelectionHandler.playerPointSelection.get(player.username)[1];
			if (pointOne.world == pointTwo.world)
			{

				SchematicMap schematic = new SchematicMap().loadWorldSelection(pointOne.world, pointOne, pointTwo);
				playerSchematic.put(player.username, schematic);

				player.sendChatToPlayer(ChatMessageComponent.createFromText("Loaded selection into memory"));
			}
			else
			{
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Selection points must be in the same world"));
			}
		}
	}

	public static void saveWorldSelection(EntityPlayer player, String name)
	{
		if (hasSchematicLoaded(player))
		{
			if (name == null)
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh.mm.ss");
				name = player.username + "_Schematic_" + dateFormat.format(new Date());
			}
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Schematic saved to .minecraft/schematics/" + name));
			getSchematic(player).saveToBaseDirectory(name);
		}
		else
		{
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Can't save! Load a selection first"));
		}
	}

	public static boolean hasSchematicLoaded(EntityPlayer player)
	{
		return player != null && playerSchematic.get(player.username) != null;
	}

	public static SchematicMap getSchematic(EntityPlayer player)
	{
		return player != null ? playerSchematic.get(player.username) : null;
	}

	@SuppressWarnings("deprecation")
	public static void getSchematic(EntityPlayer player, File file)
	{
		if (file.exists())
		{
			try
			{
				SchematicMap schematic = new SchematicMap();
				schematic.load(CompressedStreamTools.readCompressed(file.toURL().openStream()));
				playerSchematic.put(player.username, schematic);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Error loading schematic from file see  game logs for details"));
			}
		}
	}
}
