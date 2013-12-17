package artillects.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import artillects.hive.HiveComplex;
import artillects.hive.HiveComplexManager;

public class EntityDroneSelector implements IEntitySelector
{
    IArtillect drone;

    public EntityDroneSelector(IArtillect drone)
    {
        this.drone = drone;
    }

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            if (entity.isEntityAlive() && !entity.isInvisible())
            {
                // Attack players if hive drone TODO change to hostile only system later
                if (drone.getOwner() != HiveComplex.getPlayerHive())
                {
                    if (entity instanceof EntityPlayer)
                    {
                        if (!((EntityPlayer) entity).capabilities.isCreativeMode)
                        {
                            return true;
                        }
                    }
                    // Attack enemy drones
                    if (entity instanceof EntityArtillectBase)
                    {
                        if (drone.getOwner() instanceof EntityPlayer)
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    // Attack enemy drones
                    if (entity instanceof EntityArtillectBase)
                    {
                        if (drone.getOwner() != HiveComplex.getPlayerHive())
                        {
                            return true;
                        }
                    }
                }
                // Always attack mobs
                if (entity instanceof IMob)
                {
                    return true;
                }

            }
        }
        return false;
    }
}
