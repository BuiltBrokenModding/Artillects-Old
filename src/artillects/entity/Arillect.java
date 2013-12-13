package artillects.entity;

import artillects.Artillects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.EntityRegistry;

public enum Arillect
{
    WORKER("worker", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            //EntityRegistry.registerGlobalEntityID(EntityDroneWorker.class, "DroneWorker", EntityRegistry.findGlobalUniqueEntityId());
            EntityRegistry.registerModEntity(EntityWorker.class, "DroneWorker", ids++, Artillects.instance(), 64, 1, true);
            //EntityRegistry.addSpawn(Entity.class, 3, 1, 10, EnumCreatureType.creature, BiomeGenBase.forest, BiomeGenBase.river);
        }

        @Override
        public EntityArtillectBase getNew(World world)
        {
            // TODO Auto-generated method stub
            return new EntityWorker(world);
        }
    }),
    FABRICATOR("fabricator", new IDroneBuilder()
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
    }),
    CONSTRUCTOR("constructor", new IDroneBuilder()
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
    }),
    HIVEMIND("hivemind", new IDroneBuilder()
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
    }),
    DEMOLISHER("demolisher", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityCombatDrone.class, "DroneCombat", ids++, Artillects.instance(), 64, 1, true);

        }

        @Override
        public EntityArtillectBase getNew(World world)
        {
            return new EntityCombatDrone(world);
        }
    }),
    SEEKER("seeker", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            EntityRegistry.registerModEntity(EntityCombatDisc.class, "DroneDiscSmall", ids++, Artillects.instance(), 64, 1, true);

        }

        @Override
        public EntityLivingBase getNew(World world)
        {
            return new EntityCombatDisc(world);
        }
    }),
    ZOMBIE("borg", new IDroneBuilder()
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
    IDroneBuilder builder;

    public String name;

    private Arillect(String name, IDroneBuilder builder)
    {
        this.name = name;
        this.builder = builder;
    }

    public void reg()
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
