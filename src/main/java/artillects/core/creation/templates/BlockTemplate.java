package artillects.core.creation.templates;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import artillects.core.Reference;
import artillects.core.creation.Subblock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

/** Basic block template used to create single or metadata based blocks. This is not designed for any
 * advanced application but can be extended to offer more features.
 * 
 * @author Darkguardsman */
public class BlockTemplate extends Block
{
    /** Data for block metadata */
    public Subblock[] subblocks;

    public BlockTemplate(int id, Material material)
    {
        super(id, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        if (subblocks != null && subblocks[meta] != null)
        {
            Icon icon = subblocks[meta].getIcon(side);
            if (icon != null)
                return icon;
        }
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        if (subblocks != null)
        {
            for (int i = 0; i < subblocks.length; i++)
            {
                if (subblocks[i].hasSides)
                {
                    for (int side = 0; side < 6; side++)
                    {
                        if (subblocks[i].getIcon(side) == null)
                        {
                            subblocks[i].iconSide[side] = register.registerIcon(Reference.PREFIX + subblocks[i].iconSideName[side]);
                        }
                    }
                }
                else if (subblocks[i].getIcon(0) == null)
                {
                    subblocks[i].iconMain = register.registerIcon(Reference.PREFIX + subblocks[i].iconName);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        super.getSubBlocks(id, tab, list);
        if (subblocks != null)
        {
            for (int i = 0; i < subblocks.length; i++)
            {
                if (subblocks[i] != null)
                {
                    list.add(new ItemStack(id, 1, i));
                }
            }
        }
    }

}
