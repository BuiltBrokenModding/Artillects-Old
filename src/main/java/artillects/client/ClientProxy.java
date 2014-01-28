package artillects.client;

import calclavia.lib.render.FxLaser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.vector.Vector3;
import artillects.Artillects;
import artillects.CommonProxy;
import artillects.block.lightbridge.TileLightbridge;
import artillects.client.gui.GuiArtillect;
import artillects.client.render.RenderArtillectItems;
import artillects.client.render.RenderCombatDrone;
import artillects.client.render.RenderDemolisher;
import artillects.client.render.RenderFabricator;
import artillects.client.render.RenderLightbridge;
import artillects.client.render.RenderSeeker;
import artillects.client.render.RenderWorker;
import artillects.entity.IArtillect;
import artillects.entity.combat.EntityCombatDrone;
import artillects.entity.combat.EntityDemolisher;
import artillects.entity.combat.EntitySeeker;
import artillects.entity.workers.EntityFabricator;
import artillects.entity.workers.EntityWorker;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySeeker.class, new RenderSeeker());
		RenderingRegistry.registerEntityRenderingHandler(EntityDemolisher.class, new RenderDemolisher());
		RenderingRegistry.registerEntityRenderingHandler(EntityFabricator.class, new RenderFabricator());
		RenderingRegistry.registerEntityRenderingHandler(EntityWorker.class, new RenderWorker());
		RenderingRegistry.registerEntityRenderingHandler(EntityCombatDrone.class, new RenderCombatDrone());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileLightbridge.class, new RenderLightbridge());
		MinecraftForgeClient.registerItemRenderer(Artillects.itemArtillectSpawner.itemID, new RenderArtillectItems());
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == GuiIDs.ARTILLECT_ENTITY.ordinal())
		{
			Entity entity = world.getEntityByID(x);

			if (entity instanceof IArtillect)
			{
				return new GuiArtillect((IArtillect) world.getEntityByID(x), player);
			}
		}

		return null;
	}

	@Override
	public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FxLaser(world, start, end, r, g, b, 20));
	}
}
