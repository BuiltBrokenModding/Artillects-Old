package com.builtbroken.artillects.content.blocks.door;

import com.builtbroken.artillects.Artillects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/** Version of the door that is able to be locked
 * 
 * @author Darkguardsman */
public class BlockLockedDoor extends BlockDoor
{
    public BlockLockedDoor()
    {
        super(Material.rock);
        setBlockName("CustomLockedDoor");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.iron_block.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        //We have no icons to register
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return (par1 & 8) != 0 ? null : (Artillects.itemLockedDoor != null ? Artillects.itemLockedDoor : null);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World world, int x, int y, int z)
    {
        return Artillects.itemLockedDoor;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        //TODO encode nbt for placing the door with settings
        return Artillects.itemLockedDoor  != null ? new ItemStack(Artillects.itemLockedDoor) : null;
    }

    public TileEntity getTileEntity(World world, int x, int y, int z)
    {
        TileEntity ent;
        int blockMeta = world.getBlockMetadata(x, y, z);

        if ((blockMeta & 8) == 0)
        {
            ent = world.getTileEntity(x, y, z);
        }
        else
        {
            ent = world.getTileEntity(x, y - 1, z);
        }
        return ent;
    }

    public TileLockedDoor getDoorTile(World world, int x, int y, int z)
    {
        TileEntity ent = getTileEntity(world, x, y, z);
        if (ent instanceof TileLockedDoor)
        {
            return (TileLockedDoor) ent;
        }
        return null;
    }

    @Override
    public TileEntity createTileEntity(World var1, int meta)
    {
        if ((meta & 8) == 0)
            return new TileLockedDoor();
        else
            return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        activateDoor(world, x, y, z);
        return true;
    }

    /** Opens and closes the door **/
    public void activateDoor(World world, int x, int y, int z)
    {

        int fullMeta = func_150012_g(world, x, y, z);
        int newMeta = fullMeta & 7;
        newMeta ^= 4;

        if ((fullMeta & 8) == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, newMeta, 3);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.markBlockForUpdate(x, y, z);
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, newMeta, 3);
            world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
            world.markBlockForUpdate(x, y - 1, z);
        }

        world.playAuxSFXAtEntity(null, 1003, x, y, z, 0);
    }

    public boolean isDoorOpen(IBlockAccess world, int x, int y, int z)
    {
        return (func_150012_g(world, x, y, z) & 4) != 0;
    }

}
