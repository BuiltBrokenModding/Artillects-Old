package artillects.hive;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.EntityArtillectBase;
import artillects.entity.EntityFabricator;
import artillects.entity.EntityWorker;
import artillects.entity.IArtillectSpawnHandler;
import artillects.entity.combat.EntityCombatDrone;
import artillects.entity.combat.EntityDemolisher;
import artillects.entity.combat.EntitySeeker;
import cpw.mods.fml.common.registry.EntityRegistry;

public enum ArtillectEntityType
{
	WORKER("worker", new IArtillectSpawnHandler()
	{
		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntityWorker.class, "worker", ids++, Artillects.instance, 64, 1, true);
		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			return new EntityWorker(world);
		}
	}), FABRICATOR("fabricator", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntityFabricator.class, "fabricator", ids++, Artillects.instance, 64, 1, true);
		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			return new EntityFabricator(world);
		}
	}), DEMOLISHER("demolisher", new IArtillectSpawnHandler()
	{
		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntityDemolisher.class, "demolisher", ids++, Artillects.instance, 64, 1, true);
		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			return new EntityDemolisher(world);
		}
	}), SEEKER("seeker", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntitySeeker.class, "seeker", ids++, Artillects.instance, 64, 1, true);
		}

		@Override
		public EntityLivingBase getNew(World world)
		{
			return new EntitySeeker(world);
		}
	}), COMBATDRONE("combat", new IArtillectSpawnHandler()
	{
		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntityCombatDrone.class, "combatdrone", ids++, Artillects.instance, 64, 1, true);
		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			return new EntityCombatDrone(world);
		}
	});

	public static int ids = 54;
	IArtillectSpawnHandler builder;

	public final String name;

	private ArtillectEntityType(String name, IArtillectSpawnHandler builder)
	{
		this.name = name;
		this.builder = builder;
	}

	public void register()
	{
		if (builder != null)
		{
			builder.register();
		}
	}

	public EntityLivingBase getNew(World world)
	{
		if (builder != null)
		{
			return builder.getNew(world);
		}
		return null;
	}
}
