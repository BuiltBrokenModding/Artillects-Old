package artillects.drone.hive;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import artillects.drone.entity.combat.EntityDemolisher;
import artillects.drone.entity.combat.EntitySeeker;
import artillects.drone.entity.workers.EntityFabricator;
import artillects.drone.entity.workers.EntityWorker;

/** Ratio of AIs: 1 Fabricator : 5 Harvesters : 4 Blacksmith : 2 Crafters : 3 Seekers: 2 Demolisher
 * 
 * @author Calclavia */
public enum EnumArtillectType
{
    NONE(0, EntityLivingBase.class),
    FABRICATOR(1, EntityFabricator.class),
    HARVESTER(5, EntityWorker.class),
    BLACKSMITH(4, EntityWorker.class),
    CRAFTER(1, EntityWorker.class),
    SEEKER(2, EntitySeeker.class),
    DEMOLISHER(2, EntityDemolisher.class);

    public final int ratio;
    /** Entity allowed */
    public final Class<? extends Entity> entityClass;

    public final Set<ItemStack> resourcesRequired = new HashSet<ItemStack>();

    EnumArtillectType(int ratio, Class<? extends Entity> entityClass)
    {
        this.ratio = ratio;
        this.entityClass = entityClass;
    }

    public EnumArtillectType toggle(Entity entity)
    {
        int nextID = this.ordinal();

        while (true)
        {
            nextID = (nextID + 1) % values().length;

            if (EnumArtillectType.get(nextID).entityClass.isAssignableFrom(entity.getClass()))
            {
                return EnumArtillectType.get(nextID);
            }
        }
    }

    public static EnumArtillectType get(int id)
    {
        return values()[id];
    }

    public Set<ItemStack> getResourcesRequired()
    {
        return this.resourcesRequired;
    }
}