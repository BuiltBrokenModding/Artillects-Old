package artillects.drone.entity.ai.work;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import universalelectricity.api.vector.Vector3;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.ai.EntityArtillectAIBase;
import artillects.drone.entity.workers.EntityArtillectDrone;
import artillects.drone.hive.zone.ZoneProcessing;

public abstract class EntityAILaborTask extends EntityArtillectAIBase
{
    private EntityArtillectDrone drone;

    public EntityAILaborTask(EntityArtillectDrone drone, double moveSpeed)
    {
        super(drone.worldObj, moveSpeed);
        this.drone = drone;
    }

    @Override
    public EntityArtillectDrone getArtillect()
    {
        return this.drone;
    }

    /** Returns true if a chest is found and an item (ONLY ONE) is dumped. Dumps only one item at a
     * time!
     * 
     * @return True */
    protected boolean dumpInventoryToChest()
    {
        if (this.getArtillect().getZone() instanceof ZoneProcessing)
        {
            for (Vector3 chestPosition : ((ZoneProcessing) getArtillect().getZone()).chestPositions)
            {
                TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

                if (tileEntity instanceof TileEntityChest)
                {
                    TileEntityChest chest = ((TileEntityChest) tileEntity);

                    for (int i = 0; i < chest.getSizeInventory(); i++)
                    {
                        ItemStack itemStack = chest.getStackInSlot(i);

                        for (int j = 0; j < this.getArtillect().inventory.getSizeInventory(); j++)
                        {
                            ItemStack stackInEntity = this.getArtillect().inventory.getStackInSlot(j);

                            if (stackInEntity != null)
                            {
                                if (itemStack == null)
                                {
                                    if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
                                    {
                                        if (new Vector3(this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectGround.interactionDistance)
                                        {
                                            this.getArtillect().getNavigator().clearPathEntity();
                                            chest.setInventorySlotContents(i, stackInEntity);
                                            this.getArtillect().inventory.setInventorySlotContents(j, null);
                                        }
                                    }
                                }
                                else if (itemStack.isItemEqual(stackInEntity) && itemStack.stackSize < itemStack.getMaxStackSize())
                                {
                                    if (this.getArtillect().tryToWalkNextTo(chestPosition, this.moveSpeed))
                                    {
                                        if (new Vector3(this.getArtillect()).distance(chestPosition.clone().add(0.5)) <= EntityArtillectGround.interactionDistance)
                                        {
                                            int originalStackSize = itemStack.stackSize;
                                            itemStack.stackSize = Math.min(itemStack.stackSize + stackInEntity.stackSize, itemStack.getMaxStackSize());
                                            stackInEntity.stackSize -= itemStack.stackSize - originalStackSize;

                                            if (stackInEntity.stackSize <= 0)
                                            {
                                                this.getArtillect().inventory.setInventorySlotContents(j, null);
                                            }
                                        }
                                    }
                                }
                                chest.openChest();
                                this.lastUseChest = chest;
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

}
