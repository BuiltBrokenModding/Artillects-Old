package artillects.core;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import resonant.lib.render.fx.FxLaser;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b, int ticks)
    {
        EntityLivingBase renderentity = Minecraft.getMinecraft().renderViewEntity;
        if (renderentity != null)
        {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FxLaser(world, start, end, r, g, b, 20));
        }
    }
}
