package artillects.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.CommonProxy;
import artillects.CommonProxy.GuiIDs;
import artillects.client.gui.GuiWorker;
import artillects.client.render.RenderCombatDrone;
import artillects.client.render.RenderSeeker;
import artillects.container.ContainerWorker;
import artillects.entity.combat.EntityCombatDisc;
import artillects.entity.combat.EntityDemoDrone;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCombatDisc.class, new RenderSeeker());
		RenderingRegistry.registerEntityRenderingHandler(EntityDemoDrone.class, new RenderCombatDrone());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == GuiIDs.WORKER.ordinal())
		{
			return new GuiWorker(new ContainerWorker(player));
		}

		return null;
	}
}
