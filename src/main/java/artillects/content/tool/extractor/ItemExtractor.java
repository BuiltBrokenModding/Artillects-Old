package artillects.content.tool.extractor;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.content.tool.ItemPlaceableTool;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.transform.vector.Vector3;

public class ItemExtractor extends ItemPlaceableTool
{
    private HashMap<String, Long> lastUsed = new HashMap<String, Long>();
    public static Long COOLDOWN = (long) ((TileExtractor.COOLDOWN / 20) * 1000);

    public ItemExtractor(int id)
    {
        super(id);
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
