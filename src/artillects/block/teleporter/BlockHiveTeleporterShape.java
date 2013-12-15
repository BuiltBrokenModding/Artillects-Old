package artillects.block.teleporter;

import net.minecraft.block.material.Material;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.teleporter.util.Shape;

public class BlockHiveTeleporterShape extends BlockBase
{
	public BlockHiveTeleporterShape(Shape shape)
	{
		super("teleporterShape." + shape.ordinal(), Material.iron);
		setTextureName(Artillects.PREFIX + "teleporterShape_" + shape.ordinal());
	}
}
