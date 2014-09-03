package artillects.content.blocks.door;

import java.util.Random;

import artillects.core.Artillects;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/** Version of the door that is able to be locked
 * 
 * @author Darkguardsman */
public class BlockLockedDoor extends BlockDoor
{
    protected BlockLockedDoor(int id)
    {
        super(id, Material.rock);
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return (par1 & 8) != 0 ? 0 : (Artillects.itemLockedDoor != null ? Artillects.itemLockedDoor.itemID : 0);
    }

    @Override
    public int idPicked(World world, int x, int y, int z)
    {
        return Artillects.itemLockedDoor  != null ? Artillects.itemLockedDoor.itemID : 0;
    }

    public TileEntity getTileEntity(World world, int x, int y, int z)
    {
        TileEntity ent;
        int blockMeta = world.getBlockMetadata(x, y, z);

        if ((blockMeta & 8) == 0)
        {
            ent = world.getBlockTileEntity(x, y, z);
        }
        else
        {
            ent = world.getBlockTileEntity(x, y - 1, z);
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

        int fullMeta = getFullMetadata(world, x, y, z);
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

    @Override
    public void onPoweredBlockChange(World world, int x, int y, int z, boolean par5)
    {
        TileLockedDoor door = getDoorTile(world, x, y, z);
        if (door != null && door.allowRedstone)
        {
            activateDoor(world, x, y, z);
        }
    }

    public boolean isDoorOpen(IBlockAccess world, int x, int y, int z)
    {
        return (getFullMetadata(world, x, y, z) & 4) != 0;
    }

}
