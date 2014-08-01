package artillects.core.creation.templates;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import resonant.lib.render.RenderUtility;
import artillects.core.Reference;
import artillects.core.creation.ContentLoader;
import artillects.core.creation.Subblock;
import artillects.core.creation.content.ContentBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Basic block template used to create single or metadata based blocks. This is not designed for any
 * advanced application but can be extended to offer more features.
 * 
 * @author Darkguardsman */
public class BlockTemplate extends Block
{
    /** Data for the block */
    public ContentBlock content;

    public BlockTemplate(int id, Material material)
    {
        super(id, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        if (content.subBlocks != null && content.subBlocks[meta] != null)
        {
            Icon icon = content.subBlocks[meta].getIcon(side);
            if (icon != null)
                return icon;
        }
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        if (this.blockIcon == null && content.iconName != null)
        {
            if (ContentLoader.blockTextures.containsKey(content.iconName))
            {
                blockIcon = ContentLoader.blockTextures.get(content.iconName);
            }
            else if (RenderUtility.getIcon(content.iconName) != null)
            {
                blockIcon = RenderUtility.getIcon(content.iconName);
            }
            else
            {
                blockIcon = register.registerIcon((content.iconName.contains(":") ? "" : Reference.PREFIX) + content.iconName);
            }
        }
        if (content.subBlocks != null)
        {
            for (int i = 0; i < content.subBlocks.length; i++)
            {
                Subblock sub = content.subBlocks[i];
                if (sub != null)
                {
                    if (sub.hasSides)
                    {
                        for (int side = 0; side < 6; side++)
                        {
                            if (sub.getIcon(side) == null && sub.iconSideName[side] != null)
                            {
                                String name = content.subBlocks[i].iconSideName[side];
                                sub.iconSide[side] = register.registerIcon((name.contains(":") ? "" : Reference.PREFIX) + name);
                            }
                        }
                    }
                    else if (sub.getIcon(0) == null && sub.iconName != null)
                    {
                        String name = sub.iconName;
                        sub.iconMain = register.registerIcon((name.contains(":") ? "" : Reference.PREFIX) + name);
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
        if (content.subBlocks != null)
        {
            for (int i = 0; i < content.subBlocks.length; i++)
            {
                if (content.subBlocks[i] != null)
                {
                    list.add(new ItemStack(id, 1, i));
                }
            }
        }
    }

}
