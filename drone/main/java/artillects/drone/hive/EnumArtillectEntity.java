package artillects.drone.hive;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import artillects.core.interfaces.IEntityIndex;
import artillects.drone.Drone;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.combat.EntityCombatDrone;
import artillects.drone.entity.combat.EntityDemolisher;
import artillects.drone.entity.combat.EntitySeeker;
import artillects.drone.entity.workers.EntityFabricator;
import artillects.drone.entity.workers.EntityWorker;
import cpw.mods.fml.common.registry.EntityRegistry;

/** Used to quickly generate new drone types without adding data to the main mod class. As well keeps
 * things cleaner knowing exactly that goes to which entity class.
 * 
 * @author DarkGuardsman */
public enum EnumArtillectEntity
{
    WORKER("worker", new IEntityIndex()
    {
        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityWorker.class, "worker", ids++, Drone.instance, 64, 1, true);
        }

        @Override
        public EntityArtillectGround getNew(World world)
        {
            return new EntityWorker(world);
        }
    }),
    FABRICATOR("fabricator", new IEntityIndex()
    {

        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityFabricator.class, "fabricator", ids++, Drone.instance, 64, 1, true);
        }

        @Override
        public EntityArtillectGround getNew(World world)
        {
            return new EntityFabricator(world);
        }
    }),
    DEMOLISHER("demolisher", new IEntityIndex()
    {
        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityDemolisher.class, "demolisher", ids++, Drone.instance, 100, 1, true);
            //EntityRegistry.addSpawn(EntityDemolisher.class, 3, 1, 10, EnumCreatureType.creature, ar);
        }

        @Override
        public EntityArtillectGround getNew(World world)
        {
            return new EntityDemolisher(world);
        }
    }),
    SEEKER("seeker", new IEntityIndex()
    {

        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntitySeeker.class, "seeker", ids++, Drone.instance, 100, 1, true);
            //EntityRegistry.addSpawn(EntitySeeker.class, 10, 5, 30, EnumCreatureType.creature, ar);
        }

        @Override
        public EntityLivingBase getNew(World world)
        {
            return new EntitySeeker(world);
        }
    }),
    COMBATDRONE("combat", new IEntityIndex()
    {
        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityCombatDrone.class, "combatdrone", ids++, Drone.instance, 100, 1, true);
            //EntityRegistry.addSpawn(EntityCombatDrone.class, 5, 1, 10, EnumCreatureType.creature, ar);
        }

        @Override
        public EntityArtillectGround getNew(World world)
        {
            return new EntityCombatDrone(world);
        }
    });
    public static BiomeGenBase[] ar = new BiomeGenBase[] { BiomeGenBase.ocean, BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.extremeHills, BiomeGenBase.forest, BiomeGenBase.taiga, BiomeGenBase.swampland, BiomeGenBase.river, BiomeGenBase.hell, BiomeGenBase.sky, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.mushroomIsland, BiomeGenBase.mushroomIslandShore, BiomeGenBase.beach, BiomeGenBase.desertHills, BiomeGenBase.forestHills, BiomeGenBase.taigaHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.jungle, BiomeGenBase.jungleHills };

    public static int ids = 54;
    IEntityIndex builder;

    public final String name;

    private EnumArtillectEntity(String name, IEntityIndex builder)
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
