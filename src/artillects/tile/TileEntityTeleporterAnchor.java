package artillects.tile;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeDirection;
import artillects.Vector3;
import artillects.block.BlockGlyph;

/**
 * 
 * @author Archadia
 */
public class TileEntityTeleporterAnchor extends TileEntityAdvanced
{
	public static final HashSet<TileEntityTeleporterAnchor> teleporters = new HashSet<TileEntityTeleporterAnchor>();

	@Override
	public void validate()
	{
		teleporters.add(this);
		super.validate();
	}

	@Override
	public void invalidate()
	{
		teleporters.remove(this);
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
			for (TileEntityTeleporterAnchor teleporter : teleporters)
			{
				if (teleporter.getFrequency() == frequency)
				{
					if (entity.worldObj != teleporter.worldObj)
					{
						entity.travelToDimension(teleporter.worldObj.provider.dimensionId);
					}

					entity.setPosition(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord);
					return;
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

		for (int i = 1; i < 6; i++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			Vector3 position = new Vector3(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);

			Block block = Block.blocksList[this.worldObj.getBlockId((int) position.x, (int) position.y, (int) position.z)];

			if (block instanceof BlockGlyph)
			{
				int metadata = this.worldObj.getBlockMetadata((int) position.x, (int) position.y, (int) position.z);
				frequency += Math.pow(BlockGlyph.MAX_GLYPH, i - 1) * metadata;
			}
			else
			{
				return -1;
			}
		}

		return frequency;
	}
}
