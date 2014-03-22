package artillects.drone.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import artillects.drone.Drone;
import artillects.drone.hive.EnumArtillectEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Calclavia
 * 
 */
@SideOnly(Side.CLIENT)
public class RenderArtillectItems implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return item.itemID == Drone.itemArtillectSpawner.itemID;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return item.itemID == Drone.itemArtillectSpawner.itemID;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glRotatef(180, 0, 0, 1);
		if (type == ItemRenderType.INVENTORY)
		{
			GL11.glTranslatef(0, -0.9f, 0);
		}

		switch (EnumArtillectEntity.values()[item.getItemDamage()])
		{
			case WORKER:
			{
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderWorker.TEXTURE);
				RenderWorker.MODEL.render(0.0625f);
				break;
			}
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
			case COMBATDRONE:
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderCombatDrone.TEXTURE);
                RenderCombatDrone.MODEL.render(0.0625f);
                break;
            }
			default:
				break;
		}
	}
}
