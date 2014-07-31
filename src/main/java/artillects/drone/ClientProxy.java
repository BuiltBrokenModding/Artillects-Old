package artillects.drone;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import artillects.drone.client.render.RenderArtillectItems;
import artillects.drone.client.render.RenderCombatDrone;
import artillects.drone.client.render.RenderDemolisher;
import artillects.drone.client.render.RenderFabricator;
import artillects.drone.client.render.RenderSeeker;
import artillects.drone.client.render.RenderWorker;
import artillects.drone.entity.combat.EntityMinion;
import artillects.drone.entity.combat.EntitySeeker;
import artillects.drone.entity.combat.EntityShiv;
import artillects.drone.entity.workers.EntityFabricator;
import artillects.drone.entity.workers.EntityWorker;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySeeker.class, new RenderSeeker());
        RenderingRegistry.registerEntityRenderingHandler(EntityMinion.class, new RenderDemolisher());
        RenderingRegistry.registerEntityRenderingHandler(EntityFabricator.class, new RenderFabricator());
        RenderingRegistry.registerEntityRenderingHandler(EntityWorker.class, new RenderWorker());
        RenderingRegistry.registerEntityRenderingHandler(EntityShiv.class, new RenderCombatDrone());

        MinecraftForgeClient.registerItemRenderer(Drone.itemArtillectSpawner.itemID, new RenderArtillectItems());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
}
