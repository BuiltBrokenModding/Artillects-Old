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
			if (stack != null)
			{
				if (stack.stackSize <= 0)
				{
					return null;
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
			else
			{
				return null;
			}
		}

		return stack;
	}

	public static boolean isInventoryFull(IInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack != null)
			{
				if (itemStack.stackSize < 64)
				{
					return false;
				}

				continue;
			}

			return false;
		}

		return true;
	}

	public static boolean isInventoryEmpty(IInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack != null)
			{
				return false;
			}
		}

		return true;
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
