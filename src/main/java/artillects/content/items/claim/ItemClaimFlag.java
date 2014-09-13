package artillects.content.items.claim;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Item used to claim an area for a faction, or create a new town
 * 
 * @author Darkguardsman */
public class ItemClaimFlag extends Item
{
    public ItemClaimFlag()
    {
        super();
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        
        return false;
    }
}
