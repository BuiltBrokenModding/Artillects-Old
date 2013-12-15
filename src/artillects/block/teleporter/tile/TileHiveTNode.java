package artillects.block.teleporter.tile;

import net.minecraft.tileentity.TileEntity;
import artillects.Vector3;
import artillects.block.teleporter.util.Shape;
import artillects.block.teleporter.util.TeleporterCode;

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
		if (!worldObj.isRemote) {
			shapeCode = getFrameCombo();
		}
	}
	
	public TeleporterCode getFrameCombo() {
		Shape blockCombo1 = Shape.NOTHING;
		Shape blockCombo2 = Shape.NOTHING;
		Shape blockCombo3 = Shape.NOTHING;
		Shape blockCombo4 = Shape.NOTHING;
		
		if(worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord - 1) instanceof TileHiveTeleporterShape) {
			blockCombo1 = ((TileHiveTeleporterShape)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord - 1)).getShape();
		}
		if(worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord + 1) instanceof TileHiveTeleporterShape) {
			blockCombo2 = ((TileHiveTeleporterShape)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord + 1)).getShape();
		}
		if(worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord - 1) instanceof TileHiveTeleporterShape) {
			blockCombo3 = ((TileHiveTeleporterShape)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord - 1)).getShape();
		}
		if(worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord + 1) instanceof TileHiveTeleporterShape) {
			blockCombo4 = ((TileHiveTeleporterShape)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord + 1)).getShape();
		}
		if(blockCombo1 == Shape.NOTHING || blockCombo2 == Shape.NOTHING || blockCombo2 == Shape.NOTHING || blockCombo2 == Shape.NOTHING) {
			return null;
		}
		return new TeleporterCode(blockCombo1, blockCombo2, blockCombo3, blockCombo4);
	}
}
