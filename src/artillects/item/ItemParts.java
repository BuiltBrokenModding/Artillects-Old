package artillects.item;

import java.util.List;

import artillects.Artillects;
import artillects.entity.Artillect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

/** Parts and materials used in the machines.
 * 
 * @author DarkGuardsman */
public class ItemParts extends ItemBase
{
    public ItemParts()
    {
        super("DroneParts");
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() < Part.values().length)
        {
            return "item." + Part.values()[itemStack.getItemDamage()].name;
        }
        return super.getUnlocalizedName(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (Part part : Part.values())
        {
            par3List.add(new ItemStack(this, 1, part.ordinal()));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta)
    {
        if (meta < Part.values().length)
        {
            return Part.values()[meta].icon;
        }
        return this.itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        for (Part part : Part.values())
        {
            part.icon = par1IconRegister.registerIcon(Artillects.PREFIX + "dronepart." + part.name);
        }
    }

    public static enum Part
    {
        GEARS("gears"),
        MELTED_CICUITS("meltedCircuits"),
        T2_MELTED_CICUITS("meltedCircuits2"),
        T3_MELTED_CICUITS("meltedCircuits3"),
        CICUITS("circuits"),
        T2_CICUITS("circuits2"),
        T3_CICUITS("circuits3");

        public final String name;
        public Icon icon;

        private Part(String name)
        {
            this.name = name;
        }

    }
}
