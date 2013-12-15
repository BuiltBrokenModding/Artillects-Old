package artillects.block.teleporter;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.Vector3;
import artillects.block.teleporter.util.Pair;
import artillects.block.teleporter.util.Shape;

/**
 * 
 * @author Archadia
 */
public class TileGravityElev extends TileEntity
{
	TeleporterCode shapeCode;
	Vector3 vecThis = new Vector3(this);

	public TileGravityElev()
	{
		shapeCode = new TeleporterCode(Shape.NOTHING, Shape.NOTHING, Shape.NOTHING, Shape.NOTHING);
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			/*
			if (!Artillects.teleporters.containsKey(shapeCode))
			{
				Artillects.teleporters.put(shapeCode, new Pair().setLeft(vecThis));
			}
			else if (Artillects.teleporters.containsKey(shapeCode))
			{

			}*/
		}
	}
}
