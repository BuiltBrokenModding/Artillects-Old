package artillects.block.teleporter;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.IHiveBlock;

public class BlockTeleporterAnchor extends BlockBase implements ITileEntityProvider, IHiveBlock
{
    public Icon iconTop, iconSide, iconBot;

    public BlockTeleporterAnchor()
    {
        super("teleporterAnchor", Material.iron);
        this.setHardness(32F);
        this.setResistance(1000F);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3)
    {
        if (player != null && player.getHeldItem() == null || player.getHeldItem().itemID != (Artillects.blockGlyph.blockID))
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile instanceof TileEntityTeleporterAnchor)
            {
                int frequency = ((TileEntityTeleporterAnchor) tile).getFrequency();

                if (frequency == -1)
                {
                    if (!world.isRemote)
                        player.addChatMessage(Artillects.getLocal("msg.teleporter.setup"));
                }
                else
                {
                    if (player.isSneaking())
                    {
                        if (!world.isRemote)
                            player.addChatMessage(Artillects.getLocal("msg.teleporter.frequency") + " " + frequency);
                    }
                    else
                    {
                        ((TileEntityTeleporterAnchor) tile).doTeleport(player);
                    }
                }
            }
            return true;
        }

        return false;
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
