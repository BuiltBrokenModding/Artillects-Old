package artillects.core;

import artillects.content.tool.GuiPlacedTool;
import artillects.content.tool.TilePlaceableTool;
import artillects.content.tool.extractor.TileExtractor;
import artillects.content.tool.surveyor.GuiSurveyor;
import artillects.content.tool.surveyor.TileSurveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import com.builtbroken.lib.transform.vector.Vector3;

public class ClientProxy extends CommonProxy
{

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileSurveyor)
        {
            if (ID == 0)
                return new GuiSurveyor(player, (TileSurveyor) tile);
        }
        else if (tile instanceof TileExtractor)
        {
            if (ID == 0)
                return new GuiPlacedTool(player, (TilePlaceableTool) tile);
        }
        return null;
    }

    @Override
    public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b, int ticks)
    {
        EntityLivingBase renderentity = Minecraft.getMinecraft().renderViewEntity;
        if (renderentity != null)
        {
            //FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FxBeam(world, start, end, r, g, b, 20));
        }
    }
}
