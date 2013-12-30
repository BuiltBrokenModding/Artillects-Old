package artillects.block.door;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.vector.Vector3;

public class TileDoorCore extends TileEntity implements IFrameCore {

	public Vector3[] getFrameLocation() {
		Vector3[] locations = new Vector3[15];
		
		locations[0] = new Vector3(this.xCoord - 1, this.yCoord, this.zCoord);
		locations[1] = new Vector3(this.xCoord - 2, this.yCoord, this.zCoord);
		locations[2] = new Vector3(this.xCoord + 1, this.yCoord, this.zCoord);
		locations[3] = new Vector3(this.xCoord + 2, this.yCoord, this.zCoord);
		locations[4] = new Vector3(this.xCoord - 2, this.yCoord - 1, this.zCoord);
		locations[5] = new Vector3(this.xCoord + 2, this.yCoord - 1, this.zCoord);
		locations[6] = new Vector3(this.xCoord - 2, this.yCoord - 2, this.zCoord);
		locations[7] = new Vector3(this.xCoord + 2, this.yCoord - 2, this.zCoord);
		locations[8] = new Vector3(this.xCoord - 2, this.yCoord - 3, this.zCoord);
		locations[9] = new Vector3(this.xCoord + 2, this.yCoord - 3, this.zCoord);
		locations[10] = new Vector3(this.xCoord - 2, this.yCoord - 4, this.zCoord);
		locations[11] = new Vector3(this.xCoord - 1, this.yCoord - 4, this.zCoord);
		locations[12] = new Vector3(this.xCoord, this.yCoord - 4, this.zCoord);
		locations[13] = new Vector3(this.xCoord + 1, this.yCoord - 4, this.zCoord);
		locations[14] = new Vector3(this.xCoord + 2, this.yCoord - 4, this.zCoord);

		return locations;
	}
	
	public boolean isFrameComplete() {
		return true;
	}
	
	public void toggleFrame() {
		
	}
}
