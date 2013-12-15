package artillects.entity.ai;

import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.InventoryHelper;
import artillects.Vector3;
import artillects.entity.EntityArtillectBase;
import artillects.entity.EntityFabricator;
import artillects.hive.ZoneBuilding;

public class EntityAIBuilding extends EntityAIBase
{
	private EntityFabricator entity;
	private World world;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private int idleTime = 0;
	private final int maxIdleTime = 20;

	public EntityAIBuilding(EntityFabricator entity, double par2)
	{
		this.entity = entity;
		this.world = entity.worldObj;
		this.moveSpeed = par2;
		this.setMutexBits(4);
	}

	@Override
	public void startExecuting()
	{

	}

	/** Returns whether the EntityAIBase should begin execution. */
	@Override
	public boolean shouldExecute()
	{
		return this.entity.zone instanceof ZoneBuilding && !((ZoneBuilding) entity.zone).buildPosition.isEmpty();
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

		if (this.idleTime <= 0)
		{
			/**
			 * Builds the building in the order of the Hashmap
			 */
			if (((ZoneBuilding) entity.zone).buildPosition.size() > 0)
			{
				Iterator<Entry<Vector3, ItemStack>> it = ((ZoneBuilding) entity.zone).buildPosition.entrySet().iterator();

				while (it.hasNext())
				{
					Entry<Vector3, ItemStack> entry = it.next();
					ItemStack toBuildStack = entry.getValue();

					if (this.entity.hasItem(toBuildStack))
					{
						Vector3 placePosition = ((ZoneBuilding) entity.zone).resourceLocation;

						if (this.entity.tryToWalkNextTo(placePosition, this.moveSpeed))
						{
							if (placePosition.distance(new Vector3(this.entity)) <= EntityArtillectBase.interactionDistance)
							{
								this.entity.getNavigator().clearPathEntity();

								if (toBuildStack.getItem() instanceof ItemBlock)
								{
									((ItemBlock) toBuildStack.getItem()).placeBlockAt(toBuildStack, null, this.world, (int) placePosition.x, (int) placePosition.y, (int) placePosition.z, 0, 0, 0, 0, toBuildStack.getItemDamage());
									this.idleTime = this.maxIdleTime;
								}
							}
						}
					}
					else
					{
						Vector3 resourceLocation = ((ZoneBuilding) entity.zone).resourceLocation;

						if (this.entity.tryToWalkNextTo(resourceLocation, this.moveSpeed))
						{
							if (resourceLocation.distance(new Vector3(this.entity)) <= EntityArtillectBase.interactionDistance)
							{
								this.entity.getNavigator().clearPathEntity();

								TileEntity tileEntity = this.world.getBlockTileEntity((int) resourceLocation.x, (int) resourceLocation.y, (int) resourceLocation.z);

								if (tileEntity instanceof IInventory)
								{
									IInventory inv = (IInventory) tileEntity;
									int slotID = InventoryHelper.getItemIfExistsInSlot(inv, toBuildStack);

									if (slotID != -1)
									{
										ItemStack itemStack = inv.getStackInSlot(slotID);
										inv.setInventorySlotContents(slotID, this.entity.increaseStackSize(itemStack));

										this.idleTime = this.maxIdleTime;
									}
									else
									{
										continue;
									}

								}
							}
						}
					}

					break;
				}
			}
		}
	}
}
