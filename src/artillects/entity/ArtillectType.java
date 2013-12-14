package artillects.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.combat.EntityDemoDrone;
import artillects.entity.combat.EntitySeeker;
import cpw.mods.fml.common.registry.EntityRegistry;

public enum ArtillectType
{
	WORKER("worker", new IArtillectSpawnHandler()
	{
		@Override
		public void register()
		{
			// EntityRegistry.registerGlobalEntityID(EntityDroneWorker.class, "DroneWorker",
			// EntityRegistry.findGlobalUniqueEntityId());
			EntityRegistry.registerModEntity(EntityWorker.class, "ArtillectWorker", ids++, Artillects.instance(), 64, 1, true);
			// EntityRegistry.addSpawn(Entity.class, 3, 1, 10, EnumCreatureType.creature,
			// BiomeGenBase.forest, BiomeGenBase.river);
		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			// TODO Auto-generated method stub
			return new EntityWorker(world);
		}
	}), FABRICATOR("fabricator", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			// TODO Auto-generated method stub

		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			// TODO Auto-generated method stub
			return null;
		}
	}), CONSTRUCTOR("constructor", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			// TODO Auto-generated method stub

		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			// TODO Auto-generated method stub
			return null;
		}
	}), HIVEMIND("hivemind", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			// TODO Auto-generated method stub

		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			// TODO Auto-generated method stub
			return null;
		}
	}), DEMOLISHER("demolisher", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntityDemoDrone.class, "DroneCombat", ids++, Artillects.instance(), 64, 1, true);

		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			return new EntityDemoDrone(world);
		}
	}), SEEKER("seeker", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			EntityRegistry.registerModEntity(EntitySeeker.class, "DroneDiscSmall", ids++, Artillects.instance(), 64, 1, true);

		}

		@Override
		public EntityLivingBase getNew(World world)
		{
			return new EntitySeeker(world);
		}
	}), ZOMBIE("borg", new IArtillectSpawnHandler()
	{

		@Override
		public void register()
		{
			// TODO Auto-generated method stub

		}

		@Override
		public EntityArtillectBase getNew(World world)
		{
			// TODO Auto-generated method stub
			return null;
		}
	});

	public static int ids = 54;
	IArtillectSpawnHandler builder;

	public String name;

	private ArtillectType(String name, IArtillectSpawnHandler builder)
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
