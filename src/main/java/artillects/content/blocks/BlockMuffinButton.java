package artillects.content.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;

/** Random Block, Button that gives the player a muffin
 * Created by robert on 9/30/2014.
 */
public class BlockMuffinButton extends Block
{
    private final boolean pressed;

    public BlockMuffinButton(boolean pressed)
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.pressed = pressed;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z)
    {
        return null;
    }

    @Override
    public int tickRate(World world)
    {
        return this.pressed ? 30 : 20;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH)) ||
                (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (dir == WEST  && world.isSideSolid(x + 1, y, z, WEST)) ||
                (dir == EAST  && world.isSideSolid(x - 1, y, z, EAST));
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return (world.isSideSolid(x - 1, y, z, EAST)) ||
                (world.isSideSolid(x + 1, y, z, WEST)) ||
                (world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (world.isSideSolid(x, y, z + 1, NORTH));
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        int j1 = world.getBlockMetadata(x, y, z);
        int k1 = j1 & 8;
        j1 &= 7;

        ForgeDirection dir = ForgeDirection.getOrientation(side);

        if (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH))
        {
            j1 = 4;
        }
        else if (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH))
        {
            j1 = 3;
        }
        else if (dir == WEST && world.isSideSolid(x + 1, y, z, WEST))
        {
            j1 = 2;
        }
        else if (dir == EAST && world.isSideSolid(x - 1, y, z, EAST))
        {
            j1 = 1;
        }
        else
        {
            j1 = this.func_150045_e(world, x, y, z);
        }

        return j1 + k1;
    }

    private int func_150045_e(World world, int x, int y, int z)
    {
        if (world.isSideSolid(x - 1, y, z, EAST)) return 1;
        if (world.isSideSolid(x + 1, y, z, WEST)) return 2;
        if (world.isSideSolid(x, y, z - 1, SOUTH)) return 3;
        if (world.isSideSolid(x, y, z + 1, NORTH)) return 4;
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (this.func_150044_m(world, x, y, z))
        {
            int side = world.getBlockMetadata(x, y, z) & 7;
            boolean flag = false;

            if (!world.isSideSolid(x - 1, y, z, EAST) && side == 1)
            {
                flag = true;
            }

            if (!world.isSideSolid(x + 1, y, z, WEST) && side == 2)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z - 1, SOUTH) && side == 3)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z + 1, NORTH) && side == 4)
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    private boolean func_150044_m(World world, int x, int y, int z)
    {
        if (!this.canPlaceBlockAt(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        this.func_150043_b(world.getBlockMetadata(x, y, z));
    }

    private void func_150043_b(int p_150043_1_)
    {
        int j = p_150043_1_ & 7;
        boolean flag = (p_150043_1_ & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag)
        {
            f3 = 0.0625F;
        }

        if (j == 1)
        {
            this.setBlockBounds(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        }
        else if (j == 2)
        {
            this.setBlockBounds(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        }
        else if (j == 3)
        {
            this.setBlockBounds(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        }
        else if (j == 4)
        {
            this.setBlockBounds(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    @Override
    public void onBlockClicked(World w, int x, int y, int z, EntityPlayer e) {}

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        int i1 = world.getBlockMetadata(x, y, z);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);

        if (k1 == 0)
        {
            return true;
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, j1 + k1, 3);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
            this.notifyNeighbor(world, x, y, z, j1);
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
        if ((p_149749_6_ & 8) > 0)
        {
            int i1 = p_149749_6_ & 7;
            this.notifyNeighbor(world, x, y, z, i1);
        }

        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        int i1 = world.getBlockMetadata(x, y, z);

        if ((i1 & 8) == 0)
        {
            return 0;
        }
        else
        {
            int j1 = i1 & 7;
            return j1 == 5 && side == 1 ? 15 : (j1 == 4 && side == 2 ? 15 : (j1 == 3 && side == 3 ? 15 : (j1 == 2 && side == 4 ? 15 : (j1 == 1 && side == 5 ? 15 : 0))));
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random r)
    {
        if (!world.isRemote)
        {
            int l = world.getBlockMetadata(x, y, z);

            if ((l & 8) != 0)
            {
                if (this.pressed)
                {
                    this.onPressed(world, x, y, z);
                }
                else
                {
                    world.setBlockMetadataWithNotify(x, y, z, l & 7, 3);
                    int i1 = l & 7;
                    this.notifyNeighbor(world, x, y, z, i1);
                    world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
                    world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
                }
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;
        this.setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            if (this.pressed)
            {
                if ((world.getBlockMetadata(x, y, z) & 8) == 0)
                {
                    this.onPressed(world, x, y, z);
                }
            }
        }
    }


    private void onPressed(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        int i1 = meta & 7;
        boolean flag = (meta & 8) != 0;
        this.func_150043_b(meta);
        List list = world.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ));
        boolean flag1 = !list.isEmpty();

        if (flag1 && !flag)
        {
            world.setBlockMetadataWithNotify(x, y, z, i1 | 8, 3);
            this.notifyNeighbor(world, x, y, z, i1);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag1 && flag)
        {
            world.setBlockMetadataWithNotify(x, y, z, i1, 3);
            this.notifyNeighbor(world, x, y, z, i1);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag1)
        {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
        }
    }

    private void notifyNeighbor(World world, int x, int y, int z, int side)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, this);

        if (side == 1)
        {
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        }
        else if (side == 2)
        {
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        }
        else if (side == 3)
        {
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        }
        else if (side == 4)
        {
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        }
        else
        {
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {}
}
