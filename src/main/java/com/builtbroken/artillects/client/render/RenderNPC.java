package com.builtbroken.artillects.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public class RenderNPC extends RenderBiped
{
    private static final ResourceLocation steveTextures = new ResourceLocation("textures/entity/steve.png");

    public RenderNPC()
    {
        super(new ModelBiped(), 0.5F, 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return steveTextures;
    }
}
