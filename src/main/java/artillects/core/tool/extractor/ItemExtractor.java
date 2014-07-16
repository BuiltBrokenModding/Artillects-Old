package artillects.core.tool.extractor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.core.tool.ItemPlaceableTool;

public class ItemExtractor extends ItemPlaceableTool
{
    public ItemExtractor(int id)
    {
        super(id);
    }

    @Override
    public boolean used(EntityPlayer player, World world, int x, int y, int z)
    {
        return false;
    }
}
