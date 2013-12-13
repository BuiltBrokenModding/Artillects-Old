package artillects.ai;

import artillects.entity.EntityArtillectBase;
import artillects.entity.IDrone;
import artillects.hive.Hive;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;

public class EntityDroneSelector implements IEntitySelector
{
    IDrone drone;

    public EntityDroneSelector(IDrone drone)
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
                if (entity instanceof EntityArtillectBase)
                {
                    if (drone.getOwner() instanceof Hive && ((EntityArtillectBase) entity).getOwner() instanceof EntityPlayer || ((EntityArtillectBase) entity).getOwner() instanceof Hive && drone.getOwner() instanceof EntityPlayer)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
