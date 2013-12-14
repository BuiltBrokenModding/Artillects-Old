package artillects.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import artillects.Artillects;
import artillects.CommonProxy;
import artillects.client.gui.GuiWorker;
import artillects.client.render.RenderArtillectItems;
import artillects.client.render.RenderDemolisher;
import artillects.client.render.RenderSeeker;
import artillects.entity.EntityWorker;
import artillects.entity.combat.EntityDemoDrone;
import artillects.entity.combat.EntitySeeker;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySeeker.class, new RenderSeeker());
		RenderingRegistry.registerEntityRenderingHandler(EntityDemoDrone.class, new RenderDemolisher());
		MinecraftForgeClient.registerItemRenderer(Artillects.itemArtillectSpawner.itemID, new RenderArtillectItems());

	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == GuiIDs.WORKER.ordinal())
		{
			return new GuiWorker((EntityWorker) world.getEntityByID(x), player);
		}

		return null;
	}
}
