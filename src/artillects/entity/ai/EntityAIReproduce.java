package artillects.entity.ai;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.EntityArtillectBase;
import artillects.entity.EntityFabricator;
import artillects.entity.IArtillect;
import artillects.hive.ArtillectType;
import artillects.hive.Hive;
import artillects.hive.zone.ZoneProcessing;

public class EntityAIReproduce extends EntityAIBase
{
	private EntityFabricator entity;
	private World world;

	/** The speed the creature moves at during mining behavior. */
	private double moveSpeed;
	private int idleTime = 0;
	private final int maxIdleTime = 20 * 10;

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
		return true;// this.entity.zone instanceof ZoneProcessing && !((ZoneProcessing)
					// entity.zone).chestPositions.isEmpty();
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
			HashMap<ArtillectType, Integer> artillectTypeCount = new HashMap<ArtillectType, Integer>();

			Set<IArtillect> artillects = Hive.instance().getArtillects();
			synchronized (artillects)
			{
				for (IArtillect artillect : artillects)
				{
					ArtillectType type = artillect.getType();
					artillectTypeCount.put(artillect.getType(), (artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0) + 1);
				}
			}

			// TODO: Fix ratio sorting NOT working.
			for (ArtillectType type : ArtillectType.values())
			{
				int amount = artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0;

				if (amount < type.ratio)
				{
					this.tryProduce(type);
					return;
				}
			}

			this.tryProduce(ArtillectType.FABRICATOR);
			this.idleTime = this.maxIdleTime;
		}
	}

	/**
	 * Attempts to produce the Artillect of such type.
	 * 
	 * @param type
	 * @return True if produced.
	 */
	private boolean tryProduce(ArtillectType type)
	{
		if (this.entity.zone instanceof ZoneProcessing)
		{
			ZoneProcessing zone = (ZoneProcessing) this.entity.zone;

			for (ItemStack stackRequired : type.getResourcesRequired())
			{
				/**
				 * Check if we have the required resources.
				 */
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
					/**
					 * Search for the resource because we have less than the required amount.
					 */
					for (Vector3 chestPosition : zone.chestPositions)
					{
						TileEntity tileEntity = this.world.getBlockTileEntity((int) chestPosition.x, (int) chestPosition.y, (int) chestPosition.z);

						if (tileEntity instanceof TileEntityChest)
						{
							TileEntityChest chest = (TileEntityChest) tileEntity;

							for (int i = 0; i < chest.getSizeInventory(); i++)
							{
								ItemStack stackInChest = chest.getStackInSlot(i);

								if (stackInChest.isItemEqual(stackRequired))
								{
									if (this.entity.tryToWalkNextTo(chestPosition, this.moveSpeed))
									{
										if (new Vector3(this.entity).distance(chestPosition.clone().add(0.5)) <= EntityArtillectBase.interactionDistance)
										{
											this.entity.getNavigator().clearPathEntity();
											int resourceToGet = Math.max(stackRequired.stackSize - resourceCount, 0);
											chest.setInventorySlotContents(i, this.entity.increaseStackSize(stackInChest.splitStack(resourceToGet)));
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
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}
}
