package artillects.core;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.render.fx.FxLaser;
import universalelectricity.api.vector.Vector3;
import artillects.core.tool.surveyor.GuiSurveyor;
import artillects.core.tool.surveyor.TileSurveyor;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileSurveyor)
        {
            if (ID == 0)
                return new GuiSurveyor((TileSurveyor) tile);
        }
        return null;
    }
    
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
