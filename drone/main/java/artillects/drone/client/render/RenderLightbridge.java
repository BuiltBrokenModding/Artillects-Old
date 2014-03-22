package artillects.drone.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import artillects.drone.Drone;
import artillects.drone.block.lightbridge.TileLightbridge;
import artillects.drone.client.model.ModelLightbridge;

public class RenderLightbridge extends TileEntitySpecialRenderer {

	ModelLightbridge model;
	
	public RenderLightbridge() {
		model = new ModelLightbridge();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double d0, double d1, double d2, float f) {
		TileLightbridge tedc = (TileLightbridge) te;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(d0 + 0.5, d1 + 1.5, d2 + 0.5);

		ResourceLocation textures = new ResourceLocation(Drone.DOMAIN, "textures/blocks/lightbridge.png");

		bindTexture(textures);
				
		GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
		model.renderAll();
		GL11.glPopMatrix();
	}
}
