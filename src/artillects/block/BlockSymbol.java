package artillects.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import artillects.Artillects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSymbol extends BlockDecoration
{
    public BlockSymbol()
    {
        super("decorSymbol");
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (DroneSymbol data : DroneSymbol.values())
        {
            par3List.add(new ItemStack(this, 1, data.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (DroneSymbol data : DroneSymbol.values())
        {
            data.icon = par1IconRegister.registerIcon(Artillects.PREFIX + "symbol." + data.name());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        if (metadata < DroneSymbol.values().length)
        {
            return DroneSymbol.values()[metadata].icon;
        }
        return DroneSymbol.zhuang.icon;
    }

    public static enum DroneSymbol
    {
        zhuang(),
        gu(),
        zhi();
        public Icon icon;
    }

}
