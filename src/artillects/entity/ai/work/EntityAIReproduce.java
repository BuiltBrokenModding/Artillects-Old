package artillects.entity.ai.work;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import artillects.entity.EntityArtillectGround;
import artillects.entity.IArtillect;
import artillects.entity.workers.EntityFabricator;
import artillects.hive.EnumArtillectType;
import artillects.hive.zone.ZoneProcessing;

public class EntityAIReproduce extends EntityAIBase
{
    private EntityFabricator entity;
    private World world;

    /** The speed the creature moves at during mining behavior. */
    private double moveSpeed;
    private int idleTime = 0;
    private final int maxIdleTime = 500;

    public EntityAIReproduce(EntityFabricator entity, double par2)
    {
        this.entity = entity;
        this.world = entity.worldObj;
        this.moveSpeed = par2;
    }

    @Override
    public void startExecuting()
    {

    }

    /** Returns whether the EntityAIBase should begin execution. */
    @Override
    public boolean shouldExecute()
    {
        return this.entity.getZone() instanceof ZoneProcessing && this.entity.getZone().getComplex() != null;
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
        this.idleTime--;

        if (this.idleTime <= 0 && this.shouldExecute())
        {
            HashMap<EnumArtillectType, Integer> artillectTypeCount = new HashMap<EnumArtillectType, Integer>();

            Set<IArtillect> artillects = new HashSet<IArtillect>(this.entity.getZone().getComplex().getArtillects());
            artillectTypeCount.clear();
            for (IArtillect artillect : artillects)
            {
                EnumArtillectType type = artillect.getType();
                artillectTypeCount.put(artillect.getType(), (artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0) + 1);
            }

            // TODO: Fix ratio sorting NOT working.
            for (EnumArtillectType type : EnumArtillectType.values())
            {
                int amount = artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0;

                if (amount < type.ratio)
                {
                    this.tryProduce(type);
                    return;
                }
            }

            this.tryProduce(EnumArtillectType.FABRICATOR);
            this.idleTime = this.maxIdleTime;
        }
    }

    /** Attempts to produce the Artillect of such type.
     * 
     * @param type
     * @return True if produced. */
    private boolean tryProduce(EnumArtillectType type)
    {
        if (this.entity.getZone() instanceof ZoneProcessing)
        {
            ZoneProcessing zone = (ZoneProcessing) this.entity.getZone();

            for (ItemStack stackRequired : type.getResourcesRequired())
            {
                //Check if we have the required resources. 
                int resourceCount = 0;

                for (ItemStack stackInEntity : this.entity.getInventoryAsList())
                {
                    if (stackInEntity != null && stackInEntity.isItemEqual(stackRequired))
                    {
                        resourceCount += stackInEntity.stackSize;
                    }
                }

                if (resourceCount < stackRequired.stackSize)
                {
                    // Search for the resource because we have less than the required amount. 
                    for (Vector3 chestPosition : zone.chestPositions)
                    {
                        TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

                        if (tileEntity instanceof TileEntityChest)
                        {
                            TileEntityChest chest = (TileEntityChest) tileEntity;

                            for (int i = 0; i < chest.getSizeInventory(); i++)
                            {
                                ItemStack stackInChest = chest.getStackInSlot(i);

                                if (stackInChest != null && stackInChest.isItemEqual(stackRequired))
                                {
                                    if (this.entity.tryToWalkNextTo(chestPosition, this.moveSpeed))
                                    {
                                        if (new Vector3(this.entity).distance(chestPosition.clone().add(0.5)) <= EntityArtillectGround.interactionDistance)
                                        {
                                            this.entity.getNavigator().clearPathEntity();
                                            int resourceToGet = Math.max(stackRequired.stackSize - resourceCount, 0);
                                            ItemStack remainingStack = this.entity.increaseStackSize(stackInChest.splitStack(resourceToGet));
                                            chest.setInventorySlotContents(i, stackInChest.stackSize > 0 ? stackInChest : null);
                                        }
                                    }

                                    return false;
                                }
                            }

                        }

                        return false;
                    }
                }
            }
        }

        try
        {
            Entity entity = type.entityClass.getConstructor(World.class).newInstance(this.world);
            entity.setPosition(this.entity.posX, this.entity.posY, this.entity.posZ);
            this.world.spawnEntityInWorld(entity);
            ((IArtillect) entity).setType(type);
            ((IArtillect) entity).setOwner(this.entity.getOwner());
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
