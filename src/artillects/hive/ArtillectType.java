package artillects.hive;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import artillects.entity.EntityFabricator;
import artillects.entity.EntityWorker;
import artillects.entity.combat.EntityDemolisher;
import artillects.entity.combat.EntitySeeker;

/**
 * Ratio of AIs: 1 Fabricator : 5 Harvesters : 4 Blacksmith : 2 Crafters : 3 Seekers: 2 Demolisher
 * 
 * @author Calclavia
 */
public enum ArtillectType
{
	FABRICATOR(1, EntityFabricator.class), HARVESTER(5, EntityWorker.class),
	BLACKSMITH(4, EntityWorker.class), CRAFTER(1, EntityWorker.class),
	SEEKER(2, EntitySeeker.class), DEMOLISHER(2, EntityDemolisher.class);

	public final int ratio;
	/** Entity allowed */
	public final Class<? extends Entity> entityClass;

	public final Set<ItemStack> resourcesRequired = new HashSet<ItemStack>();

	ArtillectType(int ratio, Class<? extends Entity> entityClass)
	{
		this.ratio = ratio;
		this.entityClass = entityClass;
	}

	public ArtillectType toggle(Entity entity)
	{
		int nextID = this.ordinal();

		while (true)
		{
			nextID = (nextID + 1) % values().length;

			if (ArtillectType.get(nextID).entityClass.isAssignableFrom(entity.getClass()))
			{
				return ArtillectType.get(nextID);
			}
		}
	}

	public static ArtillectType get(int id)
	{
		return values()[id];
	}

	public Set<ItemStack> getResourcesRequired()
	{
		return this.resourcesRequired;
	}
}