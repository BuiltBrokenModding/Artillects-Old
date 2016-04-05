package com.builtbroken.artillects.client;

import com.builtbroken.artillects.CommonProxy;
import com.builtbroken.artillects.client.render.RenderNPC;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        //Don't super as it loads server only data
        RenderingRegistry.registerEntityRenderingHandler(EntityNpc.class, new RenderNPC());
    }
}
