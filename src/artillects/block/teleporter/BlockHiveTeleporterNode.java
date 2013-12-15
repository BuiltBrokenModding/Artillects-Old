package artillects.block.teleporter;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.block.BlockBase;

public class BlockHiveTeleporterNode extends BlockBase
{
	public BlockHiveTeleporterNode()
	{
		super("teleporterNode", Material.iron);
		setTextureName(Artillects.PREFIX + "teleporterNode");
	}

	public boolean hasTileEntity()
	{
		return true;
	}

	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileHiveTNode();
	}
}
