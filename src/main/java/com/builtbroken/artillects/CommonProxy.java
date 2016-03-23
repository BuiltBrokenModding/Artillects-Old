package com.builtbroken.artillects;

import com.builtbroken.artillects.content.tool.extractor.TileExtractor;
import com.builtbroken.artillects.content.tool.surveyor.TileSurveyor;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.awt.*;

public class CommonProxy extends AbstractProxy
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileExtractor || tile instanceof TileSurveyor)
        {
            if (ID == 0)
                return new ContainerDummy(player, tile);
        }
        return null;
    }
    
    public void renderLaser(World world, Pos start, Pos end, Color color, int ticks)
    {
        renderLaser(world, start, end, color.getRed(), color.getGreen(), color.getBlue(), ticks);
    }

    public void renderLaser(World world, Pos start, Pos end, float r, float g, float b)
    {
        renderLaser(world, start, end, r, g, b, 20);
    }

    public void renderLaser(World world, Pos start, Pos end, float r, float g, float b, int ticks)
    {

    }
}
