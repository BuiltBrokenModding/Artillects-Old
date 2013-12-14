package artillects.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import artillects.Artillects;
import artillects.client.model.ModelCombatDisc;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSeeker extends RenderLiving
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(Artillects.DOMAIN, Artillects.MODEL_DIRECTORY + "128x128Blank.png");

	public RenderSeeker()
	{
		super(new ModelCombatDisc(), 1.0F);
	}

	@Override
	protected float getDeathMaxRotation(EntityLivingBase entity)
	{
		return 180.0F;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call
	 * Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TEXTURE;
	}
}
