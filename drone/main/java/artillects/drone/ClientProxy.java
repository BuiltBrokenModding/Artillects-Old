package artillects.drone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.api.vector.Vector3;
import artillects.drone.block.lightbridge.TileLightbridge;
import artillects.drone.client.gui.GuiArtillect;
import artillects.drone.client.render.RenderArtillectItems;
import artillects.drone.client.render.RenderCombatDrone;
import artillects.drone.client.render.RenderDemolisher;
import artillects.drone.client.render.RenderFabricator;
import artillects.drone.client.render.RenderLightbridge;
import artillects.drone.client.render.RenderSeeker;
import artillects.drone.client.render.RenderWorker;
import artillects.drone.entity.IArtillect;
import artillects.drone.entity.combat.EntityCombatDrone;
import artillects.drone.entity.combat.EntityDemolisher;
import artillects.drone.entity.combat.EntitySeeker;
import artillects.drone.entity.workers.EntityFabricator;
import artillects.drone.entity.workers.EntityWorker;
import calclavia.lib.render.fx.FxLaser;
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
        MinecraftForgeClient.registerItemRenderer(Drone.itemArtillectSpawner.itemID, new RenderArtillectItems());
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
