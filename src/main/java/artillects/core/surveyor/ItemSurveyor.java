package artillects.core.surveyor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Multi-purpose tool for measuring distances, setting up areas, claiming land, or declairing tasks
 * for areas.
 * 
 * @author Darkguardsman */
public class ItemSurveyor extends ItemBlock
{
    public ItemSurveyor(int id)
    {
        super(id);
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(!player.isSneaking())
        {
            super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }
}
