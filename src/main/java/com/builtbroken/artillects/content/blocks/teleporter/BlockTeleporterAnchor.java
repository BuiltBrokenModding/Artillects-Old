package com.builtbroken.artillects.content.blocks.teleporter;

import com.builtbroken.artillects.Reference;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTeleporterAnchor extends Block implements ITileEntityProvider
{
    public IIcon iconTop, iconSide, iconBot;

    public BlockTeleporterAnchor()
    {
        super(Material.iron);
        this.setBlockName("teleporterAnchor");
        this.setHardness(32F);
        this.setResistance(1000F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.7F, 1.0F);
    }

    /** A randomly called display update to be able to add particles or other items for display */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
    {
        int l = par1World.getBlockMetadata(x, y, z);
        double spawnX = x + 0.5f;
        double spawnY = y + 0.7f + par5Random.nextFloat() * 2.5f;
        double spawnZ = z + 0.5f;
        double xRand = par5Random.nextFloat() * 0.6F - 0.3F;
        double zRand = par5Random.nextFloat() * 0.6F - 0.3F;

        par1World.spawnParticle("enchantmenttable", spawnX + xRand, spawnY, spawnZ + zRand, 0, 0.1, 0);
        par1World.spawnParticle("enchantmenttable", spawnX - xRand, spawnY, spawnZ + zRand, 0, 0.1, 0);
        par1World.spawnParticle("enchantmenttable", spawnX + xRand, spawnY, spawnZ - zRand, 0, 0.1, 0);
        par1World.spawnParticle("enchantmenttable", spawnX - xRand, spawnY, spawnZ - zRand, 0, 0.1, 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3)
    {
        if (player != null && player.getHeldItem() == null)
        {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof TileEntityTeleporterAnchor)
            {
                int frequency = ((TileEntityTeleporterAnchor) tile).getFrequency();

                if (frequency == -1)
                {
                    if (!world.isRemote)
                        player.addChatMessage(new ChatComponentText(LanguageUtility.getLocal("msg.teleporter.setup")));
                }
                else
                {
                    if (player.isSneaking())
                    {
                        if (!world.isRemote)
                        {
                            player.addChatMessage(new ChatComponentText(LanguageUtility.getLocal("msg.teleporter.frequency") + " " + frequency));
                        }
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
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity par5Entity)
    {

    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return new TileEntityTeleporterAnchor();
    }



    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityTeleporterAnchor();
    }
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int metadata)
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

    @Override
    public void registerBlockIcons(IIconRegister ir)
    {
        this.iconTop = ir.registerIcon(Reference.PREFIX + "teleporterNode_top");
        this.iconSide = ir.registerIcon(Reference.PREFIX + "teleporterNode_side");
        this.iconBot = ir.registerIcon(Reference.PREFIX + "decorWall1");
    }
}
