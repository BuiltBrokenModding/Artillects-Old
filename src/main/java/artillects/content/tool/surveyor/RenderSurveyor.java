package artillects.content.tool.surveyor;

import java.awt.Color;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import resonant.lib.render.RenderUtility;
import artillects.core.Reference;
import artillects.core.prefab.RenderTaggedTile;
import artillects.core.prefab.RenderText;

public class RenderSurveyor extends RenderTaggedTile
{
    public final static IModelCustom MODEL = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "SurveyorCam.tcn"));
    public final static ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_TEXTURE_DIRECTORY + "SurveyorCam.png");
    public final static String[] YAW_ONLY = { "base", "left", "right" };

    @Override
    public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
    {
        super.renderTileEntityAt(t, x, y, z, f);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        RenderUtility.bind(RenderSurveyor.TEXTURE);

        //YAW
        if (t instanceof TileSurveyor)
        {
            GL11.glRotated(((TileSurveyor) t).angle.yaw(), 0, 1, 0);
        }
        MODEL.renderOnly(YAW_ONLY);

        //PITCH
        if (t instanceof TileSurveyor)
        {
            GL11.glRotated(((TileSurveyor) t).angle.pitch(), 1, 0, 0);
        }
        MODEL.renderAllExcept(YAW_ONLY);

        GL11.glPopMatrix();

    }

    @Override
    protected void getRenderTags(TileEntity tile, List<RenderText> list)
    {
        if (tile instanceof TileSurveyor)
        {
            if (((TileSurveyor) tile).enabled)
            {
                double distance = ((TileSurveyor) tile).distance();
                int i = (int) (distance * 100);
                distance = i / 100;
                list.add(new RenderText("" + distance, Color.white));
                list.add(new RenderText("" + ((TileSurveyor) tile).getDirection().name(), Color.white));
            }
        }
    }

}
