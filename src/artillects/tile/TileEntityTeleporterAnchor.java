package artillects.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.ForgeDirection;
import artillects.Artillects;
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
		if(!worldObj.isRemote) {
			TeleportManager.addAnchor(this);
			for(TileEntityTeleporterAnchor te : TeleportManager.getConnectedAnchors()) System.out.println(te);
		}
		super.validate();
	}

	@Override
	public void invalidate()
	{
		if(!worldObj.isRemote) {
			TeleportManager.remAnchor(this);
			for(TileEntityTeleporterAnchor te : TeleportManager.getConnectedAnchors()) System.out.println(te);
		}
		super.invalidate();
	}

	public void refresh() {
		this.invalidate();
		this.validate();
	}
	
	@Override
	public boolean canUpdate()
	{
		return true;
	}
	
	public void updateEntity() {
		if(worldObj.getWorldTime()%2400==0) {
			refresh();
		}
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
	
	public void writeToNBT(NBTTagCompound tag) {
		
	}
}
