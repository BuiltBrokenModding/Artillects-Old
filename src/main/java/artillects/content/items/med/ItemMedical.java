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
public class ItemMedical extends Item
{
    public ItemMedical()
    {
        super();
        this.setUnlocalizedName(Reference.PREFIX + "medical");
        this.setHasSubtypes(true);
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
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if(stack.getItemDamage()<MedItem.values().length)
        {
            MedItem item = MedItem.values()[stack.getItemDamage()];
            return super.getUnlocalizedName()+ item.name().toLowerCase();
        }
        return super.getUnlocalizedName();
    }
}
