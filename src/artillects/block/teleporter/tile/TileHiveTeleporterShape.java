package artillects.block.teleporter.tile;

import artillects.block.teleporter.util.Shape;
import net.minecraft.tileentity.TileEntity;

public class TileHiveTeleporterShape extends TileEntity
{
	private Shape thisShape;

	public TileHiveTeleporterShape(Shape shape)
	{
		this.thisShape = shape;
	}

	public Shape getShape()
	{
		return thisShape;
	}

}
