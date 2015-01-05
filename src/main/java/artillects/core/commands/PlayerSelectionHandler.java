package artillects.core.commands;

import artillects.core.building.BuildFile;
import com.builtbroken.mc.lib.transform.vector.VectorWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PlayerSelectionHandler
{
	private static HashMap<String, VectorWorld[]> playerPointSelection = new HashMap<String, VectorWorld[]>();
	private static HashMap<String, BuildFile> playerSchematic = new HashMap<String, BuildFile>();

	public static VectorWorld getPointOne(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null ? playerPointSelection.get(player.getCommandSenderName())[0] : null;
	}

	public static VectorWorld getPointTwo(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null ? playerPointSelection.get(player.getCommandSenderName())[1] : null;
	}

	public static void setPointOne(EntityPlayer player, VectorWorld point)
	{
		if (player != null)
		{
			VectorWorld b = null;
			if (playerPointSelection.get(player.getCommandSenderName()) != null)
				b = playerPointSelection.get(player.getCommandSenderName())[1];

			playerPointSelection.put(player.getCommandSenderName(), new VectorWorld[] { point, b });
			if (point != null)
				player.addChatComponentMessage(new ChatComponentText("Pos one set to " + point.toString()));
		}
	}

	public static boolean hasPointOne(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null && playerPointSelection.get(player.getCommandSenderName())[0] != null;
	}

	public static void setPointTwo(EntityPlayer player, VectorWorld point)
	{
		if (player != null)
		{
			VectorWorld a = null;
			if (playerPointSelection.get(player.getCommandSenderName()) != null)
				a = playerPointSelection.get(player.getCommandSenderName())[0];

			playerPointSelection.put(player.getCommandSenderName(), new VectorWorld[] { a, point });

			if (point != null)
				player.addChatComponentMessage(new ChatComponentText("Pos two set to " + point.toString()));
		}
	}

	public static boolean hasPointTwo(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null && playerPointSelection.get(player.getCommandSenderName())[1] != null;
	}

	public static boolean hasBothPoints(EntityPlayer player)
	{
		return hasPointOne(player) && hasPointTwo(player);
	}

	public static void loadWorldSelection(EntityPlayer player)
	{
		if (hasBothPoints(player))
		{
			VectorWorld pointOne = PlayerSelectionHandler.playerPointSelection.get(player.getCommandSenderName())[0];
			VectorWorld pointTwo = PlayerSelectionHandler.playerPointSelection.get(player.getCommandSenderName())[1];
			if (pointOne.world() == pointTwo.world())
			{

				BuildFile schematic = new BuildFile().loadWorldSelection(pointOne.world(), pointOne, pointTwo);
				playerSchematic.put(player.getCommandSenderName(), schematic);

				player.addChatComponentMessage(new ChatComponentText("Loaded selection into memory"));
			}
			else
			{
				player.addChatComponentMessage(new ChatComponentText("Selection points must be in the same world"));
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
				name = player.getCommandSenderName() + "_Schematic_" + dateFormat.format(new Date());
			}
			player.addChatComponentMessage(new ChatComponentText("Schematic saved to .minecraft/schematics/" + name));
			getSchematic(player).saveToBaseDirectory(name);
		}
		else
		{
			player.addChatComponentMessage(new ChatComponentText("Can't save! Load a selection first"));
		}
	}

	public static boolean hasSchematicLoaded(EntityPlayer player)
	{
		return player != null && playerSchematic.get(player.getCommandSenderName()) != null;
	}

	public static BuildFile getSchematic(EntityPlayer player)
	{
		return player != null ? playerSchematic.get(player.getCommandSenderName()) : null;
	}

	@SuppressWarnings("deprecation")
	public static void getSchematic(EntityPlayer player, File file)
	{
		if (file.exists())
		{
			try
			{
				BuildFile schematic = new BuildFile();
				schematic.load(CompressedStreamTools.readCompressed(file.toURL().openStream()));
				playerSchematic.put(player.getCommandSenderName(), schematic);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				player.addChatComponentMessage(new ChatComponentText("Error loading schematic from file see  game logs for details"));
			}
		}
	}
}
