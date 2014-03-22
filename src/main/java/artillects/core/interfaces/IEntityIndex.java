package artillects.core.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/** Designed to be used with Enums to quickly create a way to register & generate new entities */
public interface IEntityIndex
{
    /** Called to register everything that needs to be registed for the entity to function */
    public void register();

    /** Called to create a new instance of the entity */
    public EntityLivingBase getNew(World world);
}
