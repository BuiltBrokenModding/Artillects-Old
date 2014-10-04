package artillects.content.items.med;

import artillects.core.Artillects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 10/3/2014.
 */
public enum MedItem
{
    BANDAGE(2, new ItemStack(Artillects.itemBandage, 1, 1)),
    USED_BANDAGE;

    float healBy = 2;
    ItemStack returnStack = null;

    private MedItem()
    {
        this(0, null);
    }

    private MedItem(float healBy, ItemStack used)
    {
        this.healBy = healBy;
        returnStack = used;
    }

    public static boolean canHeal(ItemStack stack, Entity ent)
    {
        if(stack != null && ent != null)
        {
            if(ent instanceof EntityPlayer)
            {
                if(((EntityPlayer) ent).capabilities.isCreativeMode)
                    return false;
                if(((EntityPlayer) ent).getHealth() < ((EntityPlayer) ent).getMaxHealth())
                    return false;
            }
            //TODO add event mods to use and hook for races to change heal method
            if(stack.getItemDamage() < MedItem.values().length)
            {
                return MedItem.values()[stack.getItemDamage()].healBy > 0;
            }
        }
        return false;
    }
}
