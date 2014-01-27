package artillects.block.lightbridge;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import artillects.Artillects;
import artillects.tile.TileEntityAdvanced;

import com.builtbroken.common.Pair;
import com.google.common.collect.HashBiMap;

public class TileLightbridgeCore extends TileEntityAdvanced {

	public boolean isToggled = false;
	
	public TileLightbridgeCore pair;
	
	public void setPair(TileLightbridgeCore toPair) {
		pair = toPair;
		toPair.pair = this;
	}
	
	public void validate() {
		super.validate();
	}
	
	public void invalidate() {
		super.invalidate();
		if(pair != null && pair.pair == this) {
			pair.pair = null;
			pair = null;
		}
	}
	
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(isFrameComplete()) {
				if(checkForLightbridge() != null) {
					TileLightbridgeCore block = (TileLightbridgeCore) worldObj.getBlockTileEntity(checkForLightbridge().right().intX(), checkForLightbridge().right().intY(), checkForLightbridge().right().intZ());
					if(block.pair == null && block.pair != this) {
						setPair(block);	
						if(isToggled = true) {
							for(int i = 1; i < Vector3.distance(new Vector3(this), checkForLightbridge().right()); i++) {
								if(checkForLightbridge().left() == ForgeDirection.NORTH) {
									if(worldObj.getBlockId(xCoord, yCoord, zCoord + i) == Artillects.blockLightbridge.blockID) {
										worldObj.setBlockToAir(xCoord, yCoord, zCoord  + i);
									}
								}
								if(checkForLightbridge().left() == ForgeDirection.SOUTH) {
									if(worldObj.getBlockId(xCoord, yCoord, zCoord - i) == Artillects.blockLightbridge.blockID) {
										worldObj.setBlockToAir(xCoord, yCoord, zCoord  - i);
									}
								}
								if(checkForLightbridge().left() == ForgeDirection.EAST) {
									if(worldObj.getBlockId(xCoord + i, yCoord, zCoord) == Artillects.blockLightbridge.blockID) {
										worldObj.setBlockToAir(xCoord + i, yCoord, zCoord);
									}
								}
								if(checkForLightbridge().left() == ForgeDirection.WEST) {
									if(worldObj.getBlockId(xCoord - i, yCoord, zCoord) == Artillects.blockLightbridge.blockID) {
										worldObj.setBlockToAir(xCoord - i, yCoord, zCoord);
									}
								}
							}
						} else {
							for(int i = 1; i < Vector3.distance(new Vector3(this), checkForLightbridge().right()); i++) {
								if(checkForLightbridge().left() == ForgeDirection.NORTH) {
									if(worldObj.getBlockId(xCoord, yCoord, zCoord + i) == 0) {
										worldObj.setBlock(xCoord, yCoord, zCoord  + i, Artillects.blockLightbridge.blockID); }
									}
								if(checkForLightbridge().left() == ForgeDirection.SOUTH) {
									if(worldObj.getBlockId(xCoord, yCoord, zCoord - i) == 0) {
										worldObj.setBlock(xCoord, yCoord, zCoord  - i, Artillects.blockLightbridge.blockID);
									}
								}
								if(checkForLightbridge().left() == ForgeDirection.EAST) {
									if(worldObj.getBlockId(xCoord + i, yCoord, zCoord) == 0) {
										worldObj.setBlock(xCoord + i, yCoord, zCoord, Artillects.blockLightbridge.blockID);
									}
								}
								if(checkForLightbridge().left() == ForgeDirection.WEST) {
									if(worldObj.getBlockId(xCoord - i, yCoord, zCoord) == 0) {
										worldObj.setBlock(xCoord - i, yCoord, zCoord, Artillects.blockLightbridge.blockID);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void toggleLightbridge() {
		if(!worldObj.isRemote) {
			if(isToggled == false) {
				isToggled = true;
				if(pair != null) pair.isToggled = true;
			} else {
				isToggled = false;
				if(pair != null) pair.isToggled = false;
			}
		}
	}
	
	public boolean isFrameComplete() {
		return true;
	}
	
	public int getFrameSize() {
		return 0;
	}

	public Pair<ForgeDirection, Vector3> checkForLightbridge() {
		
		for(int n = 1; n < 50; n++) {
			if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + n) instanceof TileLightbridgeCore) {
				Pair<ForgeDirection, Vector3> map = new Pair<ForgeDirection, Vector3>(ForgeDirection.NORTH, new Vector3(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + n)));				
				return map;
			}
		}
		for(int s = 1; s < 50; s++) {
			if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - s) instanceof TileLightbridgeCore) {
				Pair<ForgeDirection, Vector3> map = new Pair<ForgeDirection, Vector3>(ForgeDirection.SOUTH, new Vector3(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - s)));				
				return map;
			}
		}
		for(int e = 1; e < 50; e++) {
			if(worldObj.getBlockTileEntity(xCoord + e, yCoord, zCoord) instanceof TileLightbridgeCore) {
				Pair<ForgeDirection, Vector3> map = new Pair<ForgeDirection, Vector3>(ForgeDirection.EAST, new Vector3(worldObj.getBlockTileEntity(xCoord + e, yCoord, zCoord)));
				return map;
			}
		}
		for(int w = 1; w < 50; w++) {
			if(worldObj.getBlockTileEntity(xCoord - w, yCoord, zCoord) instanceof TileLightbridgeCore) {
				Pair<ForgeDirection, Vector3> map = new Pair<ForgeDirection, Vector3>(ForgeDirection.WEST, new Vector3(worldObj.getBlockTileEntity(xCoord - w, yCoord, zCoord)));
				return map;
			}
		}
		return null;
	}
	
}
