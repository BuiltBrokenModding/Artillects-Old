package com.builtbroken.artillects.content.tool.extractor;

import com.builtbroken.artillects.content.tool.ItemPlaceableTool;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class ItemExtractor extends ItemPlaceableTool
{
    private HashMap<String, Long> lastUsed = new HashMap<String, Long>();
    public static Long COOLDOWN = (long) ((TileExtractor.COOLDOWN / 20) * 1000);

    public ItemExtractor(Block block)
    {
        super(block);
    }

    @Override
    public boolean used(EntityPlayer player, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote && (!lastUsed.containsKey(player.getCommandSenderName()) || System.currentTimeMillis() - lastUsed.get(player.getCommandSenderName()) >= COOLDOWN))
        {
            TileExtractor.extractBlocks(player.inventory, world, new Pos(x, y, z), ForgeDirection.getOrientation(side), 3);
            player.inventoryContainer.detectAndSendChanges();            
            //TODO drain power
        }
        return true;
    }
}
