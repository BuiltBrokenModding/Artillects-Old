package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import com.builtbroken.artillects.core.entity.ai.AITask;
import net.minecraft.entity.EntityLivingBase;

/**
 * Simple task for attacking targets when we are in range.
 */
public class AITaskMeleeAttack extends AITask<EntityArtillect>
{
    /** An amount of decrementing ticks that allows the entity to attack once the tick reaches 0. */
    int attackTick;

    public AITaskMeleeAttack(EntityArtillect entity)
    {
        super(entity);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        if (entity().ignoreWeaponCheck || !entity().isUsingMeleeWeapon())
        {
            return;
        }

        if (entity().getAttackTarget() != null)
        {
            if (!entity().getAttackTarget().isEntityAlive())
            {
                entity().setAttackTarget(null);
                return;
            }
            EntityLivingBase entitylivingbase = this.host.getAttackTarget();
            this.host.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F); //TODO ensure we are looking at target before attacking

            //If close enough then attack
            if (--attackTick <= 0)
            {
                double distance = this.host.getDistance(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
                double attackRange = this.host.width + entitylivingbase.width;

                if (distance <= attackRange)
                {
                    this.attackTick = 10 + this.host.worldObj.rand.nextInt(10);
                    if (this.host.getHeldItem() != null)
                    {
                        this.host.swingItem();
                    }

                    this.host.attackEntityAsMob(entitylivingbase);
                    if (!entitylivingbase.isEntityAlive())
                    {
                        this.host.setAttackTarget(null);
                    }
                }
            }
        }
    }
}