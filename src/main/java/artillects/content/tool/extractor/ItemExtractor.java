package artillects.content.tool.extractor;

import artillects.content.tool.ItemPlaceableTool;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.lib.transform.vector.Vector3;

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
            TileExtractor.extractBlocks(player.inventory, world, new Vector3(x, y, z), ForgeDirection.getOrientation(side), 3);
            player.inventoryContainer.detectAndSendChanges();            
            //TODO drain power
        }
        return true;
    }
}
