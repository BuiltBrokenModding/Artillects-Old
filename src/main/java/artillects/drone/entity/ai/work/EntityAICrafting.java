package artillects.drone.entity.ai.work;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.Vector3;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.EnumArtillectType;
import artillects.drone.entity.workers.EntityWorker;
import artillects.drone.hive.zone.ZoneProcessing;

public class EntityAICrafting extends EntityAILaborTask
{
    private int idleTime = 0;
    private final int maxIdleTime = 20 * 5;

    /** ItemStack: Stack to craft
     * 
     * ItemStack[]: Recipe items */
    private final HashMap<ItemStack, ItemStack[]> stacksToCraft = new HashMap<ItemStack, ItemStack[]>();
    private boolean markForDump;

    public EntityAICrafting(EntityWorker entity, double moveSpeed)
    {
        super(entity, moveSpeed);
    }

    /** Returns whether the EntityAIBase should begin execution. */
    @Override
    public boolean shouldExecute()
    {
        return this.getArtillect().getType() == EnumArtillectType.CRAFTER && getArtillect().getZone() instanceof ZoneProcessing;
    }

    /** Returns whether an in-progress EntityAIBase should continue executing */
    @Override
    public boolean continueExecuting()
    {
        return this.shouldExecute();
    }

    /** Resets the task */
    @Override
    public void resetTask()
    {
    }

    /** Updates the task */
    @Override
    public void updateTask()
    {
        closeChest();
        this.idleTime--;

        if (this.idleTime <= 0)
        {
            if (this.markForDump)
            {
                this.markForDump = this.dumpInventoryToChest();
            }
            else
            {
                // TODO: After ModJam, make this crafting modular/work with all items.
                Iterator<Entry<ItemStack, ItemStack[]>> it = this.stacksToCraft.entrySet().iterator();

                while (it.hasNext())
                {
                    Entry<ItemStack, ItemStack[]> entry = it.next();
                    ItemStack stackToCraft = entry.getKey();
                    ItemStack[] recipeItems = entry.getValue();

                    int validItems = 0;

                    for (ItemStack recipeItem : recipeItems)
                    {
                        /** Check if we have the required resources. */
                        int resourceCount = 0;

                        for (ItemStack stackInEntity : this.getArtillect().getInventoryAsList())
                        {
                            if (stackInEntity != null && stackInEntity.isItemEqual(recipeItem))
                            {
                                resourceCount += stackInEntity.stackSize;
                            }
                        }

                        /** If we have enough resources, craft. Otherwise, go find it! If we don't
                         * find the resources, we try to craft the next item on the list. */
                        if (resourceCount < recipeItem.stackSize)
                        {
                            /** Search for the resource because we have less than the required
                             * amount. */
                            if (this.getArtillect().getZone() instanceof ZoneProcessing)
                            {
                                ZoneProcessing zone = (ZoneProcessing) this.getArtillect().getZone();

                                for (Vector3 chestPosition : zone.chestPositions)
                                {
                                    TileEntity tileEntity = world().getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

                                    if (tileEntity instanceof TileEntityChest)
                                    {
                                        TileEntityChest chest = (TileEntityChest) tileEntity;

                                        for (int i = 0; i < chest.getSizeInventory(); i++)
                                        {
                                            ItemStack stackInChest = chest.getStackInSlot(i);

                                            if (stackInChest != null && stackInChest.isItemEqual(recipeItem))
                                            {
                                                if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
                                                {
                                                    if (new Vector3((IVector3) this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectGround.interactionDistance)
                                                    {
                                                        this.getArtillect().getNavigator().clearPathEntity();
                                                        int resourceToGet = Math.max(recipeItem.stackSize - resourceCount, 0);
                                                        ItemStack remainingStack = this.getArtillect().increaseStackSize(stackInChest.splitStack(resourceToGet));
                                                        chest.setInventorySlotContents(i, stackInChest.stackSize > 0 ? stackInChest : null);
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            validItems++;
                        }
                    }

                    if (validItems >= recipeItems.length)
                    {
                        /** Do Crafting Here. */
                        for (ItemStack recipeItem : recipeItems)
                        {
                            this.getArtillect().decreaseStackSize(recipeItem);
                        }

                        this.getArtillect().increaseStackSize(stackToCraft);
                        this.markForDump = true;
                    }
                }
            }

            this.idleTime = this.maxIdleTime;
        }
    }
}
