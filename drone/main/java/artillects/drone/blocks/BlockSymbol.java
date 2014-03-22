package artillects.drone.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import artillects.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSymbol extends BlockHiveBlock
{

    public BlockSymbol(int id)
    {
        super(id, "symbol");
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (SymbolType data : SymbolType.values())
        {
            par3List.add(new ItemStack(this, 1, data.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (SymbolType data : SymbolType.values())
        {
            data.icon = par1IconRegister.registerIcon(Reference.PREFIX + "symbol." + data.name().toLowerCase());
        }

        this.blockIcon = SymbolType.ZHUANG.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        if (metadata < SymbolType.values().length)
        {
            return SymbolType.values()[metadata].icon;
        }

        return SymbolType.ZHUANG.icon;
    }

    public static enum SymbolType
    {
        ZHUANG,
        GU,
        ZHI;
        public Icon icon;
    }

}
