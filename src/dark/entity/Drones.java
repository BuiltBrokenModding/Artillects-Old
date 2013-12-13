package dark.entity;

import java.awt.Color;

import cpw.mods.fml.common.registry.EntityRegistry;
import dark.ModDrones;

public enum Drones
{
    WORKER(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            EntityRegistry.registerGlobalEntityID(EntityDroneWorker.class, "DroneWorker", EntityRegistry.findGlobalUniqueEntityId());
            EntityRegistry.registerModEntity(EntityDroneWorker.class, "DroneWorker", ids++, ModDrones.instance(), 64, 1, true);
            //EntityRegistry.addSpawn(Entity.class, 3, 1, 10, EnumCreatureType.creature, BiomeGenBase.forest, BiomeGenBase.river);
        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    FABRICATOR(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    CONSTRUCTOR(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    HIVEMIND(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    COMBAT(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    COMBAT_DISC(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }),
    ZOMBIE(new IDroneBuilder()
    {

        @Override
        public void register()
        {
            // TODO Auto-generated method stub

        }

        @Override
        public EntityDrone getNew()
        {
            // TODO Auto-generated method stub
            return null;
        }
    });
    public static int ids = 54;
    IDroneBuilder builder;

    private Drones(IDroneBuilder builder)
    {
        this.builder = builder;
    }

    public void reg()
    {
        if (builder != null)
        {
            builder.register();
        }
    }
}
