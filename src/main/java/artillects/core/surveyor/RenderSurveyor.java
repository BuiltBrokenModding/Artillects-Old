package artillects.core.surveyor;

import org.lwjgl.opengl.GL11;

import resonant.lib.render.RenderUtility;
import artillects.core.Reference;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderSurveyor extends TileEntitySpecialRenderer
{
    public final static IModelCustom MODEL = AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "SurveyorCam.tcn");
    public final static ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_TEXTURE_DIRECTORY + "SurveyorCam.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(d0 + 0.5, d1 + 0.5, d2 + 0.5);
        if(tileentity instanceof TileSurveyor)
        {
            GL11.glRotated(((TileSurveyor)tileentity).angle.yaw(), 0, 1, 0);
        }
        RenderUtility.bind(RenderSurveyor.TEXTURE);
        RenderSurveyor.MODEL.renderAll();
        GL11.glPopMatrix();
        
    }

}
