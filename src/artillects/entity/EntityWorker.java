package artillects.entity;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.entity.ai.EntityAIMining;

public class EntityWorker extends EntityArtillectBase
{
	public InventoryBasic inventory = new InventoryBasic("gui.worker", false, 1);

	public EntityWorker(World par1World)
	{
		super(par1World);
		// this.getNavigator();
		this.tasks.addTask(0, new EntityAIWander(this, 0.5f));
		this.tasks.addTask(1, new EntityAIMining(this, 1));
	}

	/** @return True if the Worker's inventory is full. (See EntityAIMining) */
	public boolean isInventoryFull()
	{
		return false;
	}

	/** @param stack */
	public void increaseStackSize(ItemStack stack)
	{
		ItemStack itemStack = this.inventory.getStackInSlot(0);

		if (itemStack == null)
		{
			this.inventory.setInventorySlotContents(0, stack);
		}
		else if (itemStack.isItemEqual(stack))
		{
			itemStack.stackSize = Math.min(itemStack.stackSize + stack.stackSize, itemStack.getMaxStackSize());
		}
	}

	@Override
	public boolean interact(EntityPlayer entityPlayer)
	{
		entityPlayer.openGui(Artillects.INSTANCE, GuiIDs.WORKER.ordinal(), this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ);
		return true;
	}
}
