package artillects.block.teleporter;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.block.BlockBase;

public class BlockGravityElev extends BlockBase
{
	public BlockGravityElev()
	{
		super("gravityElev", Material.iron);
		setTextureName(Artillects.PREFIX + "gravityElev");
	}

	public boolean hasTileEntity()
	{
		return true;
	}

	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileGravityElev();
	}
}
