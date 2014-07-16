package artillects.core.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPlaceableTool extends ItemBlock
{
    public ItemPlaceableTool(int id)
    {
        super(id);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking())
        {
            return used(player, world, x, y, z, side);
        }
        else
        {
            return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }

    public boolean used(EntityPlayer player, World world, int x, int y, int z, int side)
    {
        return false;
    }
}
