package com.builtbroken.artillects.core.integration.vanilla;

import com.builtbroken.artillects.core.integration.UsageInventory;
import com.builtbroken.artillects.core.integration.api.IUsageFueledTile;
import com.builtbroken.artillects.core.integration.api.IUsageShapelessCraftingTile;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class UsageFurnace extends UsageInventory<TileEntityFurnace> implements IUsageShapelessCraftingTile<TileEntityFurnace>, IUsageFueledTile<TileEntityFurnace>
{
    private final int[] slots = new int[]{0, 1, 2};
    private final int[] inputs = new int[]{0};
    private final int[] outputs = new int[]{2};
    private final int[] fuelSlots = new int[]{1};

    public UsageFurnace()
    {
        super("tile.usage.vanilla.furnace", "vanilla.furnace");
    }

    @Override
    public int[] getAccessableSlots(TileEntityFurnace tile)
    {
        return slots;
    }

    @Override
    public boolean isRecipe(TileEntityFurnace tile, ItemStack... items)
    {
        return items != null && items.length == 1 && FurnaceRecipes.smelting().getSmeltingResult(items[0]) != null;
    }

    @Override
    public int[] getInputSlots(TileEntityFurnace tile)
    {
        return inputs;
    }

    @Override
    public int[] getOutputSlots(TileEntityFurnace tile)
    {
        return outputs;
    }

    @Override
    public int estimateTimeLeft(TileEntityFurnace tile)
    {
        return tile.furnaceCookTime;
    }

    @Override
    public boolean isCraftingTileFunctional(TileEntityFurnace tile)
    {
        return tile.isBurning();
    }

    @Override
    public ItemStack insertItem(TileEntityFurnace tile, ItemStack insert, boolean doAction)
    {
        if (insert != null && insert.stackSize > 0)
        {
            if (tile.getStackInSlot(0) == null || InventoryUtility.stacksMatch(insert, tile.getStackInSlot(0)))
            {
                int space = tile.getStackInSlot(0) != null ? insert.getMaxStackSize() - tile.getStackInSlot(0).stackSize : insert.getMaxStackSize();
                if (space > 0)
                {
                    if (FurnaceRecipes.smelting().getSmeltingResult(insert) != null)
                    {
                        ItemStack stack = insert.copy();
                        if (stack.stackSize > space)
                        {
                            stack.stackSize--;
                            if (doAction)
                            {
                                if (tile.getStackInSlot(0) == null)
                                {
                                    ItemStack stack2 = stack.copy();
                                    stack.stackSize = space;
                                    tile.setInventorySlotContents(0, stack2);
                                }
                                else
                                {
                                    tile.getStackInSlot(0).stackSize += space;
                                }
                            }
                        }
                        else
                        {
                            if (doAction)
                            {
                                if (tile.getStackInSlot(0) == null)
                                {
                                    tile.setInventorySlotContents(0, stack);
                                }
                                else
                                {
                                    tile.getStackInSlot(0).stackSize += stack.stackSize;
                                }
                            }
                            return null;
                        }
                        return stack;
                    }
                }
            }
        }
        return insert;
    }

    @Override
    public int[] getFuelSlots(TileEntityFurnace tile)
    {
        return fuelSlots;
    }

    @Override
    public boolean isFuel(TileEntityFurnace tile, ItemStack stack)
    {
        return TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    public boolean needsFuel(TileEntityFurnace tile)
    {
        ItemStack fuel = tile.getStackInSlot(1);
        if (fuel == null)
        {
            return true;
        }
        else
        {
            int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
            burnTime = burnTime * (fuel.stackSize - 1) + tile.furnaceBurnTime;
            //Burn time for 3 items
            if (burnTime < 600)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack addFuel(TileEntityFurnace tile, ItemStack insert)
    {
        final ItemStack stack = insert.copy();
        final ItemStack fuel = tile.getStackInSlot(1);
        if (InventoryUtility.stacksMatch(stack, fuel) || fuel == null && isFuel(tile, stack))
        {
            final int space = fuel != null ? fuel.getMaxStackSize() - fuel.stackSize : stack.getMaxStackSize();
            if (fuel == null)
            {
                ItemStack stack2 = stack.copy();
                stack2.stackSize = Math.min(stack2.stackSize, space);
                tile.setInventorySlotContents(1, stack2);

                stack.stackSize -= space;
                if (stack.stackSize <= 0)
                {
                    return null;
                }
            }
            else if (stack.stackSize > space)
            {
                tile.getStackInSlot(1).stackSize += space;
                stack.stackSize -= space;
                if (stack.stackSize <= 0)
                {
                    return null;
                }
            }
            else
            {
                tile.getStackInSlot(1).stackSize += stack.stackSize;
                return null;
            }
        }
        return stack;
    }
}
