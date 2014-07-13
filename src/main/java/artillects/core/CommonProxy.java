package artillects.core;

import java.awt.Color;

import net.minecraft.world.World;
import resonant.lib.prefab.ProxyBase;
import universalelectricity.api.vector.Vector3;

public class CommonProxy extends ProxyBase
{

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
