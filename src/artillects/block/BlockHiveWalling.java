package artillects.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import artillects.Artillects;
import artillects.block.BlockSymbol.SymbolType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHiveWalling extends BlockHiveBlock
{
    public BlockHiveWalling()
    {
        super("hiveWalling");
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (HiveWallTypes data : HiveWallTypes.values())
        {
            par3List.add(new ItemStack(this, 1, data.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (HiveWallTypes data : HiveWallTypes.values())
        {
            data.icon = par1IconRegister.registerIcon(Artillects.PREFIX + "wall." + data.name().toLowerCase());
        }

        this.blockIcon = HiveWallTypes.BASE.icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        if (metadata < HiveWallTypes.values().length)
        {
            return HiveWallTypes.values()[metadata].icon;
        }

        return HiveWallTypes.BASE.icon;
    }

    public static enum HiveWallTypes
    {
        BASE,
        SQUARE;
        public Icon icon;
    }
}
