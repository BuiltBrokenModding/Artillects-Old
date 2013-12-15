package artillects.tile;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeDirection;
import artillects.TeleportManager;
import artillects.Vector3;
import artillects.block.teleporter.BlockGlyph;

/**
 * 
 * @author Archadia
 */
public class TileEntityTeleporterAnchor extends TileEntityAdvanced
{
	@Override
	public void validate()
	{
		TeleportManager.addAnchor(this);
		super.validate();
	}

	@Override
	public void invalidate()
	{
		TeleportManager.remAnchor(this);
		super.invalidate();
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	public void doTeleport(Entity entity)
	{
		int frequency = this.getFrequency();

		if (frequency > 0)
		{
			for (TileEntityTeleporterAnchor teleporter : TeleportManager.getConnectedAnchors())
			{
				if(teleporter != this) {
					if (teleporter.getFrequency() == frequency)
					{		
						entity.setPosition(teleporter.xCoord + 0.5, teleporter.yCoord + 2, teleporter.zCoord + 0.5);
						System.out.println("Entity Pos: " + entity.posX + ", " + entity.posY + ", " + entity.posZ);
						break;
					}
				}
			}			
		}
	}

	/**
	 * @return -1 if the teleporter is unable to teleport.
	 */
	public int getFrequency()
	{
		int frequency = 0;

		for (int i = 2; i < 6; i++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			Vector3 position = new Vector3(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);

			Block block = Block.blocksList[this.worldObj.getBlockId((int) position.x, (int) position.y, (int) position.z)];

			if (block instanceof BlockGlyph)
			{
				int metadata = this.worldObj.getBlockMetadata((int) position.x, (int) position.y, (int) position.z);
				frequency += Math.pow(BlockGlyph.MAX_GLYPH, i - 2) * metadata;
			}
			else
			{
				return -1;
			}
		}

		return frequency;
	}
}
