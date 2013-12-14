package artillects.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.CommonProxy;
import artillects.client.render.RenderSeeker;
import artillects.client.render.RenderCombatDrone;
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
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
