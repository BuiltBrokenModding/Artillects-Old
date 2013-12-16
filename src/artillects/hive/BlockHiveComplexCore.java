package artillects.hive;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.IHiveBlock;

public class BlockHiveComplexCore extends BlockBase implements ITileEntityProvider, IHiveBlock
{
    public Icon iconTop, iconSide, iconBot;

    public BlockHiveComplexCore()
    {
        super("HiveComplexCore", Material.iron);
        this.setBlockUnbreakable();
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityHiveComplexCore();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0)
        {
            return iconBot;
        }
        if (side == 1)
        {
            return iconTop;
        }

        return iconSide;
    }

    @Override
    public void registerIcons(IconRegister ir)
    {
        this.iconTop = ir.registerIcon(Artillects.PREFIX + "hiveCore_top");
        this.iconSide = ir.registerIcon(Artillects.PREFIX + "hiveCore_side");
        this.iconBot = ir.registerIcon(Artillects.PREFIX + "decorWall1");
    }

}
