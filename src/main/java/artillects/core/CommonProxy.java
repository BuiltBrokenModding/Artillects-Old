package artillects.core;

import artillects.content.tool.surveyor.TileSurveyor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.gui.ContainerDummy;
import resonant.lib.prefab.AbstractProxy;
import universalelectricity.core.transform.vector.Vector3;

import java.awt.*;

public class CommonProxy extends AbstractProxy
{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileSurveyor)
        {
            if (ID == 0)
                return new ContainerDummy(player, tile);
        }
        return null;
    }
    
    public void renderLaser(World world, Vector3 start, Vector3 end, Color color, int ticks)
    {
        renderLaser(world, start, end, color.getRed(), color.getGreen(), color.getBlue(), ticks);
    }

    public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b)
    {
        renderLaser(world, start, end, r, g, b, 20);
    }

    public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b, int ticks)
    {

    }
}
