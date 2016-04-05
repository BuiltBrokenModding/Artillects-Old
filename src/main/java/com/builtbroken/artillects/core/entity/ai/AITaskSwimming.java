package com.builtbroken.artillects.core.entity.ai;

import com.builtbroken.artillects.core.entity.EntityArtillect;

public class AITaskSwimming extends AITask<EntityArtillect>
{

    public AITaskSwimming(EntityArtillect entity)
    {
        super(entity);
        entity.getNavigator().setCanSwim(true);
    }

    @Override
    public void updateTask()
    {
        if (this.entity().isInWater() || entity().handleLavaMovement())
        {
            if (world().rand.nextFloat() < 0.8F)
            {
                entity().getJumpHelper().setJumping();
            }
        }
    }
}