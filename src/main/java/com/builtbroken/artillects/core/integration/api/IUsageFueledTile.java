package com.builtbroken.artillects.core.integration.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Applied to tiles that need to be fueled to function
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public interface IUsageFueledTile<T extends TileEntity>
{
    /**
     * Gets slots that can accept fuel items
     *
     * @param tile - tile getting the fuel
     * @return slots
     */
    int[] getFuelSlots(T tile);

    /**
     * Checks if the stack is fuel
     *
     * @param tile  - tile getting the fuel
     * @param stack - fuel stack
     * @return true if it will work as fuel
     */
    boolean isFuel(T tile, ItemStack stack);

    /**
     * Simple check to see if the tile needs fuel.
     *
     * @param tile - tile containing fuel
     * @return true if the machine will need fuel soon, not just when empty
     */
    boolean needsFuel(T tile);

    /**
     * Called to add fuel to the machine. No slots are used
     * as the usage class should handle the insertion. This
     * way the calling class is not guessing on slots.
     *
     * @param tile  - tile the fuel will be added to
     * @param stack - fuel stack
     * @return left over items
     */
    ItemStack addFuel(T tile, ItemStack stack);
}
