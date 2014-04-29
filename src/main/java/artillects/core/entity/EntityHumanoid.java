package artillects.core.entity;

import net.minecraft.world.World;

public class EntityHumanoid extends EntityBase
{
    public EntityHumanoid(World world)
    {
        super(world);
    }

    @Override
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
    }

    @Override
    protected boolean canDespawn()
    {
        return false;
    }
}
