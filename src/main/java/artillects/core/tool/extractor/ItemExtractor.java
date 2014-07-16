package artillects.core.tool.extractor;

import universalelectricity.api.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import artillects.core.tool.ItemPlaceableTool;

public class ItemExtractor extends ItemPlaceableTool
{
    public ItemExtractor(int id)
    {
        super(id);
    }

    @Override
    public boolean used(EntityPlayer player, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            TileExtractor.extractBlocks(player.inventory, world, new Vector3(x, y, z), ForgeDirection.getOrientation(side), 3);
            player.inventoryContainer.detectAndSendChanges();
        }
        return true;
    }
}
