package com.builtbroken.artillects.core.integration.templates;

import com.builtbroken.artillects.core.integration.UsageInventory;
import com.builtbroken.artillects.core.integration.api.IUsageStorage;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

/**
 * Simple object used to generate a basic inventory usage template
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class UsageStorage extends UsageInventory implements IUsageStorage
{
    private final int[] slots;

    public UsageStorage(String localization, String id, int[] slots)
    {
        super(localization, id);
        this.slots = slots;
    }

    @Override
    public int[] getAccessableSlots(TileEntity tile)
    {
        return slots;
    }

    @Override
    public ItemStack removeItem(TileEntity tile, ItemStack compare, boolean doAction)
    {
        if (compare != null && tile instanceof IInventory)
        {
            int desiredStack = compare.stackSize; //How may items we want
            for (int slot : getAccessableSlots(tile))
            {
                //Get stack in slot
                final ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                if (InventoryUtility.stacksMatch(compare, slotStack)) //Ensure match
                {
                    //If greater or equal than remove items from stack and finish
                    if (slotStack.stackSize >= desiredStack)
                    {
                        if (doAction)
                        {
                            slotStack.stackSize -= desiredStack;
                        }
                        desiredStack = 0;
                        break;
                    }
                    //If smaller, kill slot stack and move on
                    else
                    {
                        desiredStack -= slotStack.stackSize;
                        if (doAction)
                        {
                            ((IInventory) tile).setInventorySlotContents(slot, null);
                        }
                    }
                }
            }
            //If we found items to remove, copy compare and return with stack size found
            if (desiredStack != compare.stackSize)
            {
                ItemStack copy = compare.copy();
                copy.stackSize = compare.stackSize - desiredStack;
                return copy;
            }
        }
        return null;
    }

    @Override
    public ItemStack storeItem(TileEntity tile, ItemStack insert, boolean doAction)
    {
        if (tile instanceof IInventory)
        {
            int[] slots = getSlotsWithSpaceLeft(tile, insert);
            if (slots.length > 0)
            {
                if (!doAction)
                {
                    ItemStack copy = insert.copy();
                    for (int slot : slots)
                    {
                        ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                        if (slotStack != null)
                        {
                            copy.stackSize -= Math.max(0, Math.min(((IInventory) tile).getInventoryStackLimit(), slotStack.getMaxStackSize()) - slotStack.stackSize);
                        }
                        else
                        {
                            copy.stackSize -= ((IInventory) tile).getInventoryStackLimit();
                        }
                        if (copy.stackSize <= 0)
                        {
                            return null;
                        }
                    }
                    return copy;
                }
                return InventoryUtility.putStackInInventory((IInventory) tile, insert, getAccessableSlots(tile), false);
            }
        }
        return insert;
    }

    @Override
    public ItemStack storeItem(TileEntity tile, int[] slotInput, ItemStack insert, boolean doAction)
    {
        if (tile instanceof IInventory && slotInput.length > 0)
        {
            //Since inventory helper currently doesn't have a doAction system, we need to mimic storing the item
            if (!doAction)
            {
                int[] slots = getSlotsWithSpaceLeft((IInventory) tile, insert, slotInput);
                ItemStack copy = insert.copy();
                for (int slot : slots)
                {
                    ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                    if (slotStack != null)
                    {
                        copy.stackSize -= Math.max(0, Math.min(((IInventory) tile).getInventoryStackLimit(), slotStack.getMaxStackSize()) - slotStack.stackSize);
                    }
                    else
                    {
                        copy.stackSize -= ((IInventory) tile).getInventoryStackLimit();
                    }
                    if (copy.stackSize <= 0)
                    {
                        return null;
                    }
                }
                return copy;
            }
            return InventoryUtility.putStackInInventory((IInventory) tile, insert, slots, false);
        }
        return insert;
    }

    @Override
    public int[] getSlotsContaining(TileEntity tile, ItemStack stack)
    {
        if(tile instanceof IInventory)
        {
            ArrayList<Integer> slots = new ArrayList();
            for (int slot : getAccessableSlots(tile))
            {
                ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                if (InventoryUtility.stacksMatch(stack, slotStack))
                {
                    slots.add(slot);
                }
            }
            //TODO Might need to find a better way to do this
            if (!slots.isEmpty())
            {
                int[] array = new int[slots.size()];
                for (int i = 0; i < array.length; i++)
                {
                    array[i] = slots.get(i);
                }
                return array;
            }
        }
        return new int[0];
    }

    @Override
    public int[] getSlotsWithSpaceLeft(TileEntity tile)
    {
        if (tile instanceof IInventory)
        {
            ArrayList<Integer> slots = new ArrayList();
            for (int slot : getAccessableSlots(tile))
            {
                ItemStack stack = ((IInventory) tile).getStackInSlot(slot);
                if (stack == null || stack.stackSize < stack.getMaxStackSize())
                {
                    slots.add(slot);
                }
            }
            //TODO Might need to find a better way to do this
            if (!slots.isEmpty())
            {
                int[] array = new int[slots.size()];
                for (int i = 0; i < array.length; i++)
                {
                    array[i] = slots.get(i);
                }
                return array;
            }
        }
        return new int[0];
    }

    @Override
    public int[] getSlotsWithSpaceLeft(TileEntity tile, ItemStack stack)
    {
        //Same as getSlotWithSpace(Tile) but matches stack param with slot stack
        if (tile instanceof IInventory)
        {
            return getSlotsWithSpaceLeft((IInventory) tile, stack, getAccessableSlots(tile));
        }
        return new int[0];
    }

    protected int[] getSlotsWithSpaceLeft(IInventory inv, ItemStack stack, int[] slotInput)
    {
        ArrayList<Integer> slots = new ArrayList();
        for (int slot : slotInput)
        {
            ItemStack slotStack = inv.getStackInSlot(slot);
            if (slotStack == null || InventoryUtility.stacksMatch(stack, slotStack) && slotStack.stackSize < slotStack.getMaxStackSize())
            {
                slots.add(slot);
            }
        }
        //TODO Might need to find a better way to do this
        if (!slots.isEmpty())
        {
            int[] array = new int[slots.size()];
            for (int i = 0; i < array.length; i++)
            {
                array[i] = slots.get(i);
            }
            return array;
        }
        return new int[0];
    }

    @Override
    public int getStorageSpace(TileEntity tile)
    {
        if (tile instanceof IInventory)
        {
            return ((IInventory) tile).getInventoryStackLimit() * ((IInventory) tile).getSizeInventory();
        }
        return 0;
    }

    @Override
    public int getSpaceLeft(TileEntity tile)
    {
        int space = 0;
        if (tile instanceof IInventory)
        {
            for (int slot : getAccessableSlots(tile))
            {
                ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                if (slotStack != null)
                {
                    space += Math.max(0, Math.min(((IInventory) tile).getInventoryStackLimit(), slotStack.getMaxStackSize()) - slotStack.stackSize);
                }
                else
                {
                    space += ((IInventory) tile).getInventoryStackLimit();
                }
            }
        }
        return space;
    }

    @Override
    public int getCount(TileEntity tile, ItemStack stack)
    {
        int count = 0;
        if (tile instanceof IInventory)
        {
            for (int slot : getAccessableSlots(tile))
            {
                ItemStack slotStack = ((IInventory) tile).getStackInSlot(slot);
                if (InventoryUtility.stacksMatch(stack, slotStack))
                {
                    count += slotStack.stackSize;
                }
            }
        }
        return count;
    }
}
