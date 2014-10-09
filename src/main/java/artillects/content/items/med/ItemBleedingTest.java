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
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!world.isRemote && !player.isPotionActive(PotionBleeding.INSTANCE.getId()))
        {
            player.addPotionEffect(new PotionEffect(PotionBleeding.INSTANCE.getId(), 500));
        }
        return true;
    }
}
