package artillects.core.creation.templates;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import artillects.core.Reference;
import artillects.core.creation.ContentFactory;
import artillects.core.creation.Subblock;
import artillects.core.creation.content.BlockProduct;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Basic block template used to create single or metadata based blocks. This is not designed for any
 * advanced application but can be extended to offer more features.
 * 
 * @author Darkguardsman */
public class BlockTemplate extends Block
{
    /** Data for the block */
    public BlockProduct content;

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
        if (content.iconName != null)
        {
            if (ContentFactory.blockTextures.containsKey(content.iconName))
            {
                blockIcon = ContentFactory.blockTextures.get(content.iconName);
            }
            else
            {
                blockIcon = register.registerIcon((content.iconName.contains(":") ? "" : Reference.PREFIX) + content.iconName);
            }
        }
        else
        {
            blockIcon = register.registerIcon(Reference.PREFIX + "lightbridgeCore_top");
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
                            if (sub.iconSideName[side] != null)
                            {
                                String name = sub.iconSideName[side];
                                if (ContentFactory.blockTextures.containsKey(name))
                                {
                                    sub.iconSide[side] = ContentFactory.blockTextures.get(name);
                                }
                                else
                                {
                                    sub.iconSide[side] = register.registerIcon((name.contains(":") ? "" : Reference.PREFIX) + name);
                                }
                            }
                        }
                    }
                    else if (sub.iconName != null)
                    {
                        if (ContentFactory.blockTextures.containsKey(sub.iconName))
                        {
                            sub.iconMain = ContentFactory.blockTextures.get(sub.iconName);
                        }
                        else
                        {
                            sub.iconMain = register.registerIcon((sub.iconName.contains(":") ? "" : Reference.PREFIX) + sub.iconName);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z)
    {        
        int meta = world.getBlockMetadata(x, y, z);
        if(content.subBlocks != null && content.subBlocks[meta] != null && content.subBlocks[meta].hardness != -2)
        {
            return content.subBlocks[meta].hardness;
        }
        return super.getBlockHardness(world, x, y, z);
    }
    
    @Override
    public float getExplosionResistance(Entity entity, World world,int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(content.subBlocks != null && content.subBlocks[meta] != null && content.subBlocks[meta].resistance != -2)
        {
            return content.subBlocks[meta].resistance;
        }
        return super.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
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
