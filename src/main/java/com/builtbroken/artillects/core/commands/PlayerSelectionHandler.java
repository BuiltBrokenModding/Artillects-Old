package com.builtbroken.artillects.core.commands;


import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.world.schematic.SchematicMap;
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
	private static HashMap<String, Location[]> playerPointSelection = new HashMap<String, Location[]>();
	private static HashMap<String, SchematicMap> playerSchematic = new HashMap<String, SchematicMap>();

	public static Location getPointOne(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null ? playerPointSelection.get(player.getCommandSenderName())[0] : null;
	}

	public static Location getPointTwo(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null ? playerPointSelection.get(player.getCommandSenderName())[1] : null;
	}

	public static void setPointOne(EntityPlayer player, Location point)
	{
		if (player != null)
		{
			Location b = null;
			if (playerPointSelection.get(player.getCommandSenderName()) != null)
				b = playerPointSelection.get(player.getCommandSenderName())[1];

			playerPointSelection.put(player.getCommandSenderName(), new Location[] { point, b });
			if (point != null)
				player.addChatComponentMessage(new ChatComponentText("Pos one set to " + point.toString()));
		}
	}

	public static boolean hasPointOne(EntityPlayer player)
	{
		return playerPointSelection.get(player.getCommandSenderName()) != null && playerPointSelection.get(player.getCommandSenderName())[0] != null;
	}

	public static void setPointTwo(EntityPlayer player, Location point)
	{
		if (player != null)
		{
			Location a = null;
			if (playerPointSelection.get(player.getCommandSenderName()) != null)
				a = playerPointSelection.get(player.getCommandSenderName())[0];

			playerPointSelection.put(player.getCommandSenderName(), new Location[] { a, point });

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
			Location pointOne = PlayerSelectionHandler.playerPointSelection.get(player.getCommandSenderName())[0];
			Location pointTwo = PlayerSelectionHandler.playerPointSelection.get(player.getCommandSenderName())[1];
			if (pointOne.world() == pointTwo.world())
			{

				SchematicMap schematic = new SchematicMap().loadWorldSelection(pointOne.world(), pointOne, pointTwo);
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

	public static SchematicMap getSchematic(EntityPlayer player)
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
                SchematicMap schematic = new SchematicMap();
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
