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
    //Single bandage
    /* 0 */BANDAGE(2, new ItemStack(Artillects.itemBandage, 1, 1)),
    /* 1 */USED_BANDAGE,

    //Package of bandages
    /* 2 */BANDAGE_PACK_SIX(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 3, 1)),
    /* 3 */BANDAGE_PACK_FIVE(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 4, 1)),
    /* 4 */BANDAGE_PACK_FOUR(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 5, 1)),
    /* 5 */BANDAGE_PACK_THREE(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 6, 1)),
    /* 6 */BANDAGE_PACK_TWO(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 7, 1)),
    /* 7 */BANDAGE_PACK_ONE(2, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 8, 1)),
    /* 8 */BANDAGE_PACK_ZERO;

    float healBy = 2;
    ItemStack[] returnStack = null;

    private MedItem()
    {
        this(0, null);
    }

    private MedItem(float healBy, ItemStack... used)
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
                if(((EntityPlayer) ent).getHealth() >= ((EntityPlayer) ent).getMaxHealth())
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
