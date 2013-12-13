package dark.drones.ai;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import dark.drones.entity.EntityDrone;
import dark.drones.hive.Hive;

public class EntityDroneSelector implements IEntitySelector
{
    EntityDrone drone;

    public EntityDroneSelector(EntityDrone drone)
    {
        this.drone = drone;
    }

    /** Return whether the specified entity is applicable to this filter. */
    public boolean isEntityApplicable(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            if (entity.isEntityAlive())
            {
                //Attack players if hive drone TODO change to hostile only system later
                if (drone.getOwner() instanceof Hive)
                {
                    if (entity instanceof EntityPlayer)
                    {
                        if (!((EntityPlayer) entity).capabilities.isCreativeMode)
                        {
                            return true;
                        }
                    }
                }
                //Always attack mobs
                if (entity instanceof IMob)
                {
                    return true;
                }
                //Attack enemy drones
                if (entity instanceof EntityDrone)
                {
                    if (drone.getOwner() instanceof Hive && ((EntityDrone) entity).getOwner() instanceof EntityPlayer || ((EntityDrone) entity).getOwner() instanceof Hive && drone.getOwner() instanceof EntityPlayer)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
