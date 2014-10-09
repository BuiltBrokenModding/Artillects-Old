package artillects.content.items.med;

import artillects.content.potion.PotionBleeding;
import artillects.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.HashMap;
import java.util.List;

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
        this.setMaxStackSize(10);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if(!world.isRemote)
        {
            if (!useDelay.containsKey(player) || System.currentTimeMillis() - useDelay.get(player) >= 15000)
            {
                if (MedItem.canHeal(itemStack, player))
                {
                    MedItem item = MedItem.values()[itemStack.getItemDamage()];

                    //Remove bleeding effect
                    player.removePotionEffect(PotionBleeding.BLEEDING.getId());
                    float infectionChance = 0f;
                    float bloodPosioningChance = 0f;
                    if(item == MedItem.USED_BANDAGE)
                    {
                        infectionChance = .3f;
                        bloodPosioningChance = .03f;
                    }
                    else if(item == MedItem.USED_BANDAGE_2)
                    {
                        infectionChance = .6f;
                        bloodPosioningChance = .2f;
                    }
                    else if(item == MedItem.USED_BANDAGE_3)
                    {
                        infectionChance = .8f;
                        bloodPosioningChance = .4f;
                    }
                    else if(item == MedItem.USED_BANDAGE_4)
                    {
                        infectionChance = 1f;
                        bloodPosioningChance = .6f;
                    }
                    if(world.rand.nextFloat() <= infectionChance)
                    {
                        player.addPotionEffect(new PotionEffect(PotionBleeding.INFECTION.getId(), 5000));
                    }
                    if(world.rand.nextFloat() <= bloodPosioningChance)
                    {
                        player.addPotionEffect(new PotionEffect(PotionBleeding.BLOOD_POISONING.getId(), 5000));
                    }
                    //Heal player
                    player.heal(item.healBy);

                    //Consume Item
                    itemStack.stackSize--;
                    if(itemStack.stackSize <= 0)
                    {
                        itemStack = null;
                    }

                    //Apply Delay
                    useDelay.put(player, System.currentTimeMillis());

                    //Drop used items
                    if (item.returnStack != null)
                    {
                        for (ItemStack stack : item.returnStack)
                        {
                            if (!player.inventory.addItemStackToInventory(stack))
                            {
                                InventoryUtility.dropItemStack(new VectorWorld(player), stack);
                            }
                        }
                    }
                }
            }
        }
        return true;
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

    public void getSubItems(Item item_, CreativeTabs tab, List list)
    {
        for(MedItem item : MedItem.values())
        {
            list.add(new ItemStack(item_, 1, item.ordinal()));
        }
    }
}
