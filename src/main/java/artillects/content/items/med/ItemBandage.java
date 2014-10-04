package artillects.content.items.med;

import artillects.core.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.core.transform.vector.VectorWorld;

/** WW1 style bandage
 *
 * Created on 10/3/2014.
 * @author TheCowGod
 */
public class ItemBandage extends Item
{
    public ItemBandage()
    {
        super();
        this.setUnlocalizedName(Reference.PREFIX + "bandage");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if(MedItem.canHeal(itemStack, player))
        {
            MedItem item = MedItem.values()[itemStack.getItemDamage()];
            player.heal(item.healBy);
            itemStack.stackSize--;
            if(item.returnStack != null)
            {
                if(!player.inventory.addItemStackToInventory(item.returnStack))
                {
                    InventoryUtility.dropItemStack(new VectorWorld(player), item.returnStack);
                }
            }
        }
        return itemStack;
    }
}
