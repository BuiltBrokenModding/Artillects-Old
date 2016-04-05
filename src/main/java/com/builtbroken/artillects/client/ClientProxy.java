package com.builtbroken.artillects.client;

import com.builtbroken.artillects.CommonProxy;
import com.builtbroken.artillects.content.npc.EntityCombatTest;
import com.builtbroken.artillects.content.render.RenderBiped;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.model.ModelBiped;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        //Don't super as it loads server only data
        RenderingRegistry.registerEntityRenderingHandler(EntityCombatTest.class, new RenderBiped(new ModelBiped(), 0.5F, 1.0F));
    }
}
