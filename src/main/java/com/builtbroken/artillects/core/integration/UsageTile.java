package com.builtbroken.artillects.core.integration;

import net.minecraft.tileentity.TileEntity;

/**
 * NPC understanding of how to use a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public abstract class UsageTile<T extends TileEntity> extends Usage
{
    public UsageTile(String localization, String id)
    {
        super(localization, "tile." + id);
    }

    public abstract boolean doesApplyTo(TileEntity tile);
}
