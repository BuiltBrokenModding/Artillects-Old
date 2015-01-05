package artillects.core.building;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import com.builtbroken.lib.type.Pair;
import com.builtbroken.lib.transform.vector.VectorWorld;

/**
 * Created by robert on 9/15/2014.
 */
public class BuildingBlock extends Pair<ItemStack, VectorWorld> {

    public BuildingBlock(ItemStack left, VectorWorld right)
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
