package com.builtbroken.artillects.content.items.med;

import com.builtbroken.artillects.content.potion.PotionMedical;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by robert on 10/4/2014.
 */
public class ItemBleedingTest extends Item
{
    public ItemBleedingTest()
    {
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase attacker, EntityLivingBase entity)
    {
        if(entity != null && !entity.isPotionActive(PotionMedical.BLEEDING.getId()))
        {
            entity.addPotionEffect(new PotionEffect(PotionMedical.BLEEDING.getId(), 500));
        }
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!world.isRemote && !player.isPotionActive(PotionMedical.BLEEDING.getId()))
        {
            player.addPotionEffect(new PotionEffect(PotionMedical.BLEEDING.getId(), 500));
            player.addChatComponentMessage(new ChatComponentText("Bleeding effect added"));
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean p_77663_5_)
    {
        if(!world.isRemote)
        {
            if (slot < 9 && entity instanceof EntityPlayer)
            {
                if (((EntityPlayer) entity).isPotionActive(PotionMedical.BLEEDING))
                {
                    ((EntityPlayer) entity).addChatComponentMessage(new ChatComponentText("Bleeding: " + ((EntityPlayer) entity).getActivePotionEffect(PotionMedical.BLEEDING).getDuration()));
                }
            }
        }
    }


    @Override
    public IIcon getIconFromDamage(int d)
    {
        return Items.iron_sword.getIconFromDamage(d);
    }
}
