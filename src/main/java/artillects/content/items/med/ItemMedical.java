package artillects.content.items.med;

import artillects.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.HashMap;

/** WW1 style bandage
 *
 * Created on 10/3/2014.
 * @author TheCowGod
 */
public class ItemMedical extends Item
{
    public static HashMap<EntityPlayer, Long> useDelay = new HashMap();

    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    public ItemMedical()
    {
        super();
        this.setUnlocalizedName(Reference.PREFIX + "medical");
        this.setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if(!useDelay.containsKey(player) || System.currentTimeMillis() - useDelay.get(player) >= 15000)
        {
            if (MedItem.canHeal(itemStack, player))
            {
                MedItem item = MedItem.values()[itemStack.getItemDamage()];
                player.heal(item.healBy);
                itemStack.stackSize--;
                useDelay.put(player, System.currentTimeMillis());
                if (item.returnStack != null)
                {
                    if (!player.inventory.addItemStackToInventory(item.returnStack))
                    {
                        InventoryUtility.dropItemStack(new VectorWorld(player), item.returnStack);
                    }
                }
            }
        }
        return itemStack;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        if(meta < icons.length)
        {
            return icons[meta];
        }
        return this.itemIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.icons = new IIcon[MedItem.values().length];
        for(MedItem item : MedItem.values())
        {
            icons[item.ordinal()] = reg.registerIcon(Reference.PREFIX + "medical_" + item.name().toLowerCase());
        }
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
