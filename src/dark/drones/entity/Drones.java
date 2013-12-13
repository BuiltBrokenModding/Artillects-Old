package dark.drones.entity;

import net.minecraft.world.World;
import cpw.mods.fml.common.registry.EntityRegistry;
import dark.drones.ModDrones;

public enum Drones
{
    WORKER("drone_worker", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            //EntityRegistry.registerGlobalEntityID(EntityDroneWorker.class, "DroneWorker", EntityRegistry.findGlobalUniqueEntityId());
            EntityRegistry.registerModEntity(EntityDroneWorker.class, "DroneWorker", ids++, ModDrones.instance(), 64, 1, true);
            //EntityRegistry.addSpawn(Entity.class, 3, 1, 10, EnumCreatureType.creature, BiomeGenBase.forest, BiomeGenBase.river);
        }

        @Override
        public EntityDrone getNew(World world)
        {
            // TODO Auto-generated method stub
            return new EntityDroneWorker(world);
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
        public EntityDrone getNew(World world)
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
        public EntityDrone getNew(World world)
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
        public EntityDrone getNew(World world)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    COMBAT("combat_drone", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew(World world)
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    COMBAT_DISC("drone_disc", new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew(World world)
        {
            // TODO Auto-generated method stub
            return null;
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
        public EntityDrone getNew(World world)
        {
            // TODO Auto-generated method stub
            return null;
        }
    });
    public static int ids = 54;
    IDroneBuilder builder;

    public String name;

    private Drones(String name, IDroneBuilder builder)
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

    public EntityDrone getNew(World world)
    {
        if (builder != null)
        {
            return builder.getNew(world);
        }
        return null;
    }
}
