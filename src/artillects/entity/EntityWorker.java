package artillects.entity;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.Vector3;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ZoneMining;

public class EntityWorker extends EntityArtillectBase
{
    public enum EnumWorkerType
    {
        HARVESTER;
    }

    public InventoryBasic inventory = new InventoryBasic("gui.worker", false, 9);

    public static final int DATA_TYPE_ID = 12;

    public boolean init = false;

    public EntityWorker(World par1World)
    {
        super(par1World);
        this.tasks.addTask(0, new EntityAIMining(this, 1));
        this.tasks.addTask(1, new EntityAIWander(this, 0.5f));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(DATA_TYPE_ID, (byte) EnumWorkerType.HARVESTER.ordinal());
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (!init)
        {
            init = true;
            this.setZone(new ZoneMining(this.worldObj, new Vector3(this).add(-5), new Vector3(this).add(5)));
        }
    }

    /** @return True if the Worker's inventory is full. (See EntityAIMining) */
    public boolean isInventoryFull()
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

    /** @param stack */
    public void increaseStackSize(ItemStack stack)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack == null)
            {
                this.inventory.setInventorySlotContents(i, stack);
                return;
            }
            else if (itemStack.isItemEqual(stack))
            {
                itemStack.stackSize = Math.min(itemStack.stackSize + stack.stackSize, itemStack.getMaxStackSize());
                return;
            }
        }
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(Artillects.INSTANCE, GuiIDs.WORKER.ordinal(), this.worldObj, this.entityId, 0, 0);
        return true;
    }
}
