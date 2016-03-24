package com.builtbroken.artillects.core.integration.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.Map;

/**
 * Applied to any tile Usage
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public interface IUsageShapedCraftingTile<T extends TileEntity> extends IUsageCraftingTile<T>
{
    /**
     * Checks if the items in the map result in a recipe output
     *
     * @param tile         - tile crafting
     * @param slotsToItems - map of slots to items
     * @return true if results in a recipe
     */
    boolean isRecipe(T tile, Map<Integer, ItemStack> slotsToItems);
}
