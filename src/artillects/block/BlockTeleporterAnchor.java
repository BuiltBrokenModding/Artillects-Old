package artillects.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.tile.TileEntityTeleporterAnchor;

public class BlockTeleporterAnchor extends BlockBase implements ITileEntityProvider
{
	public Icon iconTop, iconSide, iconBot;

	public BlockTeleporterAnchor()
	{
		super("teleporterAnchor", Material.iron);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3)
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if (tile instanceof TileEntityTeleporterAnchor)
			{
				int frequency = ((TileEntityTeleporterAnchor) tile).getFrequency();

				if (frequency == -1)
				{
					player.addChatMessage(Artillects.getLocal("msg.teleporter.setup"));
				}
				else
				{
					player.addChatMessage(Artillects.getLocal("msg.teleporter.frequency") + " " + frequency);
				}
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityTeleporterAnchor();
	}

	public Icon getIcon(int side, int metadata)
	{
		if (side == 0)
		{
			return iconBot;
		}
		if (side == 1)
		{
			return iconTop;
		}

		return iconSide;
	}

	public void registerIcons(IconRegister ir)
	{
		this.iconTop = ir.registerIcon(Artillects.PREFIX + "teleporterNode_top");
		this.iconSide = ir.registerIcon(Artillects.PREFIX + "teleporterNode_side");
		this.iconBot = ir.registerIcon(Artillects.PREFIX + "decorWall1");
	}

}
