package artillects;

import java.util.List;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author Calclavia
 * 
 */
public class InventoryHelper
{
	/**
	 * Adds stack to inventory
	 * 
	 * @param stack - The stack to add
	 * @return - The remaining stack.
	 */
	public static ItemStack addStackToInventory(IInventory inventory, ItemStack stack)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (stack == null)
			{
				break;
			}

			if (stack.stackSize <= 0)
			{
				stack = null;
			}

			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack == null)
			{
				inventory.setInventorySlotContents(i, stack);
				stack = null;
			}
			else if (itemStack.isItemEqual(stack))
			{
				int originalStackSize = itemStack.stackSize;
				itemStack.stackSize = Math.min(itemStack.stackSize + stack.stackSize, itemStack.getMaxStackSize());
				stack.stackSize -= itemStack.stackSize - originalStackSize;
			}
		}

		return stack;
	}

	public static boolean listContainsStack(Set<ItemStack> compareStacks, ItemStack stack)
	{
		return InventoryHelper.getListContainsStack(compareStacks, stack) != null;
	}

	public static ItemStack getListContainsStack(Set<ItemStack> compareStacks, ItemStack stack)
	{
		for (ItemStack checkStack : compareStacks)
		{
			if (checkStack.isItemEqual(stack))
			{
				return stack;
			}
		}
		return null;
	}

	public static boolean listContainsStack(Set<ItemStack> compareStacks, List<ItemStack> checkStacks)
	{
		return InventoryHelper.getListContainsStack(compareStacks, checkStacks) != null;
	}

	public static ItemStack getListContainsStack(Set<ItemStack> compareStacks, List<ItemStack> checkStacks)
	{
		for (ItemStack compareStack : compareStacks)
		{
			for (ItemStack check : checkStacks)
			{
				if (check != null && check.isItemEqual(compareStack))
				{
					return check;
				}
			}
		}
		return null;
	}

}
