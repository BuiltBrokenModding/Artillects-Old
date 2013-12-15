package artillects.block.teleporter;

import net.minecraft.tileentity.TileEntity;
import artillects.Vector3;
import artillects.block.teleporter.util.Pair;
import artillects.block.teleporter.util.Shape;
import artillects.block.teleporter.util.TeleporterHandler;

/**
 * 
 * @author Archadia
 */
public class TileHiveTNode extends TileEntity
{
	TeleporterCode shapeCode;
	Vector3 vecThis = new Vector3(this);

	public TileHiveTNode()
	{
		shapeCode = new TeleporterCode(Shape.NOTHING, Shape.NOTHING, Shape.NOTHING, Shape.NOTHING);
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{

		}
	}
}
