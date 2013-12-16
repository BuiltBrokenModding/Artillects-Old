package artillects.entity.ai;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import artillects.entity.EntityFabricator;
import artillects.entity.IArtillect;
import artillects.hive.ArtillectTaskType;
import artillects.hive.Hive;
import artillects.hive.zone.ZoneBuilding;
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
			HashMap<ArtillectTaskType, Integer> artillectTypeCount = new HashMap<ArtillectTaskType, Integer>();

			for (IArtillect artillect : Hive.instance().getArtillects())
			{
				ArtillectTaskType type = artillect.getType();
				artillectTypeCount.put(artillect.getType(), (artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0) + 1);
			}

			for (ArtillectTaskType type : ArtillectTaskType.values())
			{
				int amount = artillectTypeCount.containsKey(type) ? artillectTypeCount.get(type) : 0;

				if (amount < type.ratio)
				{
					this.tryProduce(type);
					return;
				}
			}

			this.tryProduce(ArtillectTaskType.FABRICATOR);
			this.idleTime = this.maxIdleTime;
		}
	}

	/**
	 * Attempts to produce the Artillect of such type.
	 * 
	 * @param type
	 */
	private boolean tryProduce(ArtillectTaskType type)
	{
		if (true)
		{
			try
			{
				Entity entity = type.entityClass.getConstructor(World.class).newInstance(this.world);
				entity.setPosition(this.entity.posX, this.entity.posY, this.entity.posZ);
				this.world.spawnEntityInWorld(entity);
				((IArtillect) entity).setType(type);
				return true;
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}

		return false;
	}
}
