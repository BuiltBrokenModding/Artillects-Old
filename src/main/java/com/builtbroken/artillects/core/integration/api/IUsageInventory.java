package com.builtbroken.artillects.core.integration.api;

import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2016.
 */
public interface IUsageInventory<T extends TileEntity>
{
    /**
     * Called to get all slots that can be access by an NPC or player.
     * Normally return all slots that would show in an GUI or
     * all sides that return slots to automation.
     *
     * @param tile - tile being access
     * @return array of slots
     */
    int[] getAccessableSlots(T tile);
}
