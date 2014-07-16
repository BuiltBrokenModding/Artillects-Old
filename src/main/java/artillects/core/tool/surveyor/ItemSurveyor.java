package artillects.core.tool.surveyor;

import artillects.core.tool.ItemPlaceableTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Multi-purpose tool for measuring distances, setting up areas, claiming land, or declairing tasks
 * for areas.
 * 
 * @author Darkguardsman */
public class ItemSurveyor extends ItemPlaceableTool
{
    public ItemSurveyor(int id)
    {
        super(id);
    }
    
    @Override
    public boolean used(EntityPlayer player, World world, int x, int y, int z, int side)
    {
        return false;
    }
}
