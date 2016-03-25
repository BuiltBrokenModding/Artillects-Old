package com.builtbroken.artillects.core.integration.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Apply to any usage that describes a tile only used for storage. Do not apply to crafting based machines that happen to have storage. As
 * this could result in unwanted behavior. For example storing food in an ore grinder that has an extra larger output storage.
 * <p/>
 * Do not try to cache any values from a tile in the usage class or other system.
 * Only cache values that will never change about a tile, for example slots.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2016.
 */
public interface IUsageStorage<T extends TileEntity> extends IUsageInventory<T>
{
    /**
     * Called to store an item inside the tile.
     *
     * @param tile     - tile getting the items
     * @param compare  - item being removed
     * @param doAction - true will actually apply changes, false will test
     * @return desired stack trying to match stack size the best
     */
    ItemStack removeItem(T tile, ItemStack compare, boolean doAction);

    /**
     * Called to store an item inside the tile.
     *
     * @param tile     - tile getting the items
     * @param insert   - item being added
     * @param doAction - true will actually apply changes, false will test
     * @return remaining items
     */
    ItemStack storeItem(T tile, ItemStack insert, boolean doAction);

    /**
     * Called to store an item inside the tile within specific slots.
     *
     * @param tile     - tile getting the items
     * @param slots    - slot ids to store items into
     * @param insert   - item being added
     * @param doAction - true will actually apply changes, false will test
     * @return remaining items
     */
    ItemStack storeItem(T tile, int[] slots, ItemStack insert, boolean doAction);

    /**
     * Gets an array of slots containing the stack.
     *
     * @param tile  - tile being accessed
     * @param stack - compare stack, ignore stacksize
     * @return array of items
     */
    int[] getSlotsContaining(T tile, ItemStack stack);

    /**
     * Gets an array of slots with space for storage. Normally
     * this is passed into {@link #storeItem(TileEntity, int[], ItemStack, boolean)}.
     *
     * @param tile - tile being accessed
     * @return array of items
     */
    int[] getSlotsWithSpaceLeft(T tile);

    /**
     * Gets an array of slots with space for storage. Normally
     * this is passed into {@link #storeItem(TileEntity, int[], ItemStack, boolean)}.
     *
     * @param tile  - tile being accessed
     * @param stack - item being stored, ignore stack size
     * @return array of items
     */
    int[] getSlotsWithSpaceLeft(T tile, ItemStack stack);

    /**
     * Called to get the max number of space for storing
     * items. Should come out to Slots * SlotMaxStackSize
     *
     * @param tile - tile being accessed
     * @return space in items that can be stored, not slots
     */
    int getStorageSpace(T tile);

    /**
     * Called to get space left in the tile.
     * Should come out to MaxSpace - UsedSpace
     *
     * @param tile - tile being accessed
     * @return space in items left, not slots
     */
    int getSpaceLeft(T tile);

    /**
     * Gets a count of the number of items
     * stored.
     *
     * @param tile  - tile being accessed
     * @param stack - compare stack, ignore stack size
     * @return count of items
     */
    int getCount(T tile, ItemStack stack);

}
