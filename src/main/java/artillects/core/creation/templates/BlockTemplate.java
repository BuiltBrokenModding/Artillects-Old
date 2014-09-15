package artillects.core.creation.templates;

import artillects.core.Reference;
import artillects.core.creation.Subblock;
import artillects.core.creation.content.BlockProduct;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/** Basic block template used to create single or metadata based blocks. This is not designed for any
 * advanced application but can be extended to offer more features.
 * 
 * @author Darkguardsman */
public class BlockTemplate extends Block
{
    /** Data for the block */
    public BlockProduct content;

    public BlockTemplate(Material material)
    {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {

        if (content.subBlocks != null && content.subBlocks[meta] != null)
        {
            IIcon icon = content.subBlocks[meta].getIcon(side);
            if (icon != null)
                return icon;
        }
        return this.blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        if (content.iconName != null)
        {
                blockIcon = register.registerIcon((content.iconName.contains(":") ? "" : Reference.PREFIX) + content.iconName);
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
                                    sub.iconSide[side] = register.registerIcon((name.contains(":") ? "" : Reference.PREFIX) + name);

                            }
                        }
                    }
                    else if (sub.iconName != null)
                    {
                            sub.iconMain = register.registerIcon((sub.iconName.contains(":") ? "" : Reference.PREFIX) + sub.iconName);

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
    public void getSubBlocks(Item id, CreativeTabs tab, List list)
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
