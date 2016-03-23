package com.builtbroken.artillects.content.items.med;

import com.builtbroken.artillects.Artillects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by robert on 10/3/2014.
 */
public enum MedItem
{
    //Single bandage
    /* 0 */BANDAGE(4, new ItemStack(Artillects.itemBandage, 1, 1)),
    /* 1 */USED_BANDAGE(3, new ItemStack(Artillects.itemBandage, 8, 1)),

    //Package of bandages
    /* 2 */BANDAGE_PACK_SIX(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 3, 1)),
    /* 3 */BANDAGE_PACK_FIVE(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 4, 1)),
    /* 4 */BANDAGE_PACK_FOUR(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 5, 1)),
    /* 5 */BANDAGE_PACK_THREE(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 6, 1)),
    /* 6 */BANDAGE_PACK_TWO(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 7, 1)),
    /* 7 */BANDAGE_PACK_ONE(4, new ItemStack(Artillects.itemBandage, 1, 1), new ItemStack(Artillects.itemBandage, 8, 1)),
    /* 8 */BANDAGE_PACK_ZERO,

    /* 8 */USED_BANDAGE_2(3, new ItemStack(Artillects.itemBandage, 9, 1)),
    /* 9 */USED_BANDAGE_3(3, new ItemStack(Artillects.itemBandage, 10, 1)),
    /* 10 */USED_BANDAGE_4,
    /* 11 */CLEANED_BANDAGE(4, new ItemStack(Artillects.itemBandage, 1, 1)),
    /* 12 */BLEACHED_BANDAGE(4, new ItemStack(Artillects.itemBandage, 1, 1));

    float healBy = 2;
    ItemStack[] returnStack = null;

    MedItem()
    {
        this(0);
    }

    MedItem(float healBy, ItemStack... used)
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
