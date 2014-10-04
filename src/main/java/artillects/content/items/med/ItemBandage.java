package artillects.content.items.med;

import artillects.core.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by COWPIE on 10/3/2014.
 */
public class ItemBandage extends Item
{
    protected float healBy = 2;

    public ItemBandage()
    {
        super();
        this.setUnlocalizedName(Reference.PREFIX + "bandage");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if(!player.capabilities.isCreativeMode && player.getHealth() < player.getMaxHealth())
        {
            player.heal(healBy);
            itemStack.stackSize--;
        }
        return itemStack;
    }
}
