package artillects.hive;

import net.minecraft.entity.Entity;
import artillects.entity.EntityFabricator;
import artillects.entity.EntityWorker;
import artillects.entity.combat.EntityDemolisher;
import artillects.entity.combat.EntitySeeker;

/**
 * Ratio of AIs: 1 Fabricator : 5 Harvesters : 4 Blacksmith : 2 Crafters : 3 Seekers: 2 Demolisher
 * 
 * @author Calclavia
 */
public enum ArtillectTaskType
{
	FABRICATOR(1, EntityFabricator.class), HARVESTER(5, EntityWorker.class),
	BLACKSMITH(4, EntityWorker.class), CRAFTER(3, EntityWorker.class),
	SEEKER(2, EntitySeeker.class), DEMOLISHER(2, EntityDemolisher.class);

	public final int ratio;
	/** Entity allowed */
	public final Class<? extends Entity> entityClass;

	ArtillectTaskType(int ratio, Class<? extends Entity> entityClass)
	{
		this.ratio = ratio;
		this.entityClass = entityClass;
	}

	public ArtillectTaskType toggle(Entity entity)
	{
		int nextID = this.ordinal();

		while (true)
		{
			nextID = (nextID + 1) % values().length;

			if (entity.getClass().isAssignableFrom(this.entityClass))
			{
				return ArtillectTaskType.get(nextID);
			}
		}
	}

	public static ArtillectTaskType get(int id)
	{
		return values()[id];
	}
}