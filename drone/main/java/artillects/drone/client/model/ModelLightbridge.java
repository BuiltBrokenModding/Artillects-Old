package artillects.drone.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLightbridge extends ModelBase {
	ModelRenderer Body;
  
	public ModelLightbridge() {
		textureWidth = 128;
		textureHeight = 32;
    
		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(0F, 0F, 0F, 16, 3, 16);
		Body.setRotationPoint(-8F, 8F, -8F);
		Body.setTextureSize(128, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
	}
  
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Body.render(f5);
	}
	
	public void renderAll() {
		Body.render(0.0625F);
	}
  
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
  
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
