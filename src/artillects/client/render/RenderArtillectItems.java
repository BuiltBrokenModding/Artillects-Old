package artillects.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import artillects.Artillects;
import artillects.entity.ArtillectType;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * @author Calclavia
 * 
 */
public class RenderArtillectItems implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return item.itemID == Artillects.itemArtillectSpawner.itemID;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return item.itemID == Artillects.itemArtillectSpawner.itemID;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glRotatef(180, 0, 0, 1);
		if (type == ItemRenderType.INVENTORY)
		{
			GL11.glTranslatef(0, -0.9f, 0);
		}

		switch (ArtillectType.values()[item.getItemDamage()])
		{
			case FABRICATOR:
			{
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderFabricator.TEXTURE);
				RenderFabricator.MODEL.setRotationAngles(0, 0, 0, 0, 0, 0, null);
				RenderFabricator.MODEL.render(0.0625f);
				break;
			}
			case SEEKER:
			{
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderSeeker.TEXTURE);
				RenderSeeker.MODEL.render(0.0625f);
				break;
			}
			case DEMOLISHER:
			{
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderDemolisher.TEXTURE);
				RenderDemolisher.MODEL.render(0.0625f);
				break;
			}
			default:
				break;
		}
	}
}
