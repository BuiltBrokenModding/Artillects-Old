package artillects.block.teleporter;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.teleporter.tile.TileHiveTeleporterShape;
import artillects.block.teleporter.util.Shape;

public class BlockHiveTeleporterShape extends BlockBase
{
	private Shape blkShape;
	
	public BlockHiveTeleporterShape(Shape shape)
	{
		super("teleporterShape." + shape.ordinal(), Material.iron);
		setTextureName(Artillects.PREFIX + "teleporterShape_" + shape.ordinal());
		this.blkShape = shape;
	}
	
	public boolean hasTileEntity() {
		return true;
	}
	
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileHiveTeleporterShape(this.blkShape);
	}
}
