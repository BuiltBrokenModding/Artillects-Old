package com.builtbroken.artillects.core.integration.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Applied to tile usage that can result in a crafting result. Do not try to cache any values from a tile in the usage class or other system.
 * Only cache values that will never change about a tile, for example slots.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public interface IUsageCraftingTile<T extends TileEntity>
{
    /**
     * Slots used to input items into the recipe
     *
     * @return slot ids
     */
    int[] getInputSlots(T tile);

    /**
     * Slots used to output items from a recipe
     *
     * @return slot ids
     */
    int[] getOutputSlots(T tile);

    /**
     * Estimates how long is left on the crafting.
     * Negative -1 will assume instance, -2 will
     * assume no way to track.
     *
     * @return time left in ticks
     */
    int estimateTimeLeft(T tile);

    /**
     * Checks if the machine can function. Used
     * to do full checks and other requirements.
     * For example power, multi-block loading,
     * chunk loading, etc. Is not used
     * to check recipe matches or timers.
     *
     * @return true if can craft
     */
    boolean isCraftingTileFunctional(T tile);

    /**
     * Called to add an item to the system. The implementing class
     * should handle which slot the items goes into. This way
     * the calling class doesn't have to guess.
     *
     * @param insert   - item to insert
     * @param doAction - if true the action should be preformed, false is used to
     *                 check if the machine can accept before committing action.
     * @return left over amount of stack
     */
    ItemStack insertItem(T tile, ItemStack insert, boolean doAction);
}
