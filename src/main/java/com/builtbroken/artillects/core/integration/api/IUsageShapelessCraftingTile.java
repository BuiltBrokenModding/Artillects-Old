package com.builtbroken.artillects.core.integration.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Applied to any tile Usage
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public interface IUsageShapelessCraftingTile<T extends TileEntity> extends IUsageCraftingTile<T>
{
    /**
     * Checks if the recipe matches any recipe for the tile
     * in it's current settings.
     *
     * @param items - array of items
     * @return true if it matches, ignore stack size
     */
    boolean isRecipe(T tile, ItemStack... items);
}
