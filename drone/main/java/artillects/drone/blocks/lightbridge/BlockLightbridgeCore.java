package artillects.drone.blocks.lightbridge;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import artillects.core.Reference;
import artillects.drone.blocks.BlockBase;
import artillects.drone.blocks.IHiveBlock;

public class BlockLightbridgeCore extends BlockBase implements ITileEntityProvider, IHiveBlock
{

    Icon[] icons = new Icon[3];

    public BlockLightbridgeCore(int id)
    {
        super(id, "lightbridgeCore", Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileLightbridgeCore();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3)
    {
        TileLightbridgeCore tile = (TileLightbridgeCore) world.getBlockTileEntity(x, y, z);

        if (!world.isRemote)
            tile.toggle();

        return true;
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return icons[0];
        }
        if (side == 2)
        {
            return icons[1];
        }
        return icons[2];
    }

    @Override
    public void registerIcons(IconRegister ir)
    {
        this.icons[0] = ir.registerIcon(Reference.PREFIX + "lightbridgeCore_top");
        this.icons[1] = ir.registerIcon(Reference.PREFIX + "lightbridgeCore_front");
        this.icons[2] = ir.registerIcon(Reference.PREFIX + "decorWall.0");
    }

}
