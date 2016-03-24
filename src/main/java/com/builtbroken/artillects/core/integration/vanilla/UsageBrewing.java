package com.builtbroken.artillects.core.integration.vanilla;

import com.builtbroken.artillects.core.integration.UsageInventory;
import com.builtbroken.artillects.core.integration.api.IUsageShapelessCraftingTile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;

/**
 * NPC understanding of how to use a brewing stand and what crafting recipes work
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class UsageBrewing extends UsageInventory implements IUsageShapelessCraftingTile<TileEntityBrewingStand>
{
    public UsageBrewing()
    {
        super("tile.usage.vanilla.brewing.stand", "vanilla.brewing.stand");
    }

    @Override
    public int[] getInputSlots(TileEntityBrewingStand tile)
    {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public int[] getOutputSlots(TileEntityBrewingStand tile)
    {
        return new int[]{0, 1, 2};
    }

    @Override
    public int estimateTimeLeft(TileEntityBrewingStand tile)
    {
        return tile.getBrewTime();
    }

    @Override
    public boolean isCraftingTileFunctional(TileEntityBrewingStand tile)
    {
        return true;
    }

    @Override
    public ItemStack insertItem(TileEntityBrewingStand tile, ItemStack insert, boolean doAction)
    {
        return null;
    }

    @Override
    public boolean isRecipe(TileEntityBrewingStand tile, ItemStack... items)
    {
        boolean potionItem = false;
        for(ItemStack stack : items)
        {

        }
        return false;
    }

    @Override
    public int[] getAccessableSlots(TileEntity tile)
    {
        return new int[]{0, 1, 2, 3};
    }
}
