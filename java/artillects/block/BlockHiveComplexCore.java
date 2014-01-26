package artillects.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHiveComplexCore extends BlockBase implements ITileEntityProvider, IHiveBlock
{
	public BlockHiveComplexCore()
	{
		super("hiveCore", Material.iron);
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
}
