package artillects.block.teleporter;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.Vector3;

/**
 * 
 * @author Archadia
 */
public class TileGravityElev extends TileEntity {

	TeleporterCode code;
	
	public TileGravityElev() {
		code = new TeleporterCode(Shape.NOTHING, Shape.NOTHING, Shape.NOTHING, Shape.NOTHING);
	}
}
