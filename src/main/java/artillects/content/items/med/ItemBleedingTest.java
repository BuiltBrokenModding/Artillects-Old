package artillects.content.items.med;

import artillects.content.potion.PotionBleeding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by robert on 10/4/2014.
 */
public class ItemBleedingTest extends Item
{
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        player.addPotionEffect(new PotionEffect(PotionBleeding.INSTANCE.getId(), 500));
        return itemStack;
    }
}
