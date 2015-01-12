package artillects.core.building;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.lib.transform.vector.Location;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 9/15/2014.
 */
public class BuildingBlock extends Pair<ItemStack, Location>
{

    public BuildingBlock(ItemStack left, Location right)
    {
        super(left, right);
    }

    public ItemStack placedItem()
    {
        return left();
    }

    public Block getBlock()
    {
        if(placedItem() != null && placedItem().getItem() instanceof ItemBlock)
        {
            return Block.getBlockFromItem(placedItem().getItem());
        }
        return null;
    }

    public int getMeta()
    {
        if(placedItem() != null)
        {
            return placedItem().getItemDamage();
        }
        return 0;
    }

    /** Places the item stack at the location. */
    public void place()
    {

    }
}
