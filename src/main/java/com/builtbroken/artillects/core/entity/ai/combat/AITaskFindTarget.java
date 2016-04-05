package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import com.builtbroken.artillects.core.entity.ai.AITask;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * AI task used to find new targets for attacking.
 */
public class AITaskFindTarget extends AITask<EntityArtillect>
{
    /** Timer between search calls */
    private int searchTimer = 0;
    /** Selector to validate targets */
    IEntitySelector selector;

    public AITaskFindTarget(EntityArtillect host, IEntitySelector selector)
    {
        super(host);
        this.selector = selector;
    }

    /**
     * Distance to target entities
     *
     * @return range, defaults to 16 or follow range
     */
    protected double getTargetDistance()
    {
        IAttributeInstance iattributeinstance = entity().getEntityAttribute(SharedMonsterAttributes.followRange);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    @Override
    public void updateTask()
    {
        if (entity().getAttackTarget() == null || !entity().getAttackTarget().isEntityAlive() || !isSuitableTarget(entity().getAttackTarget()))
        {
            searchTimer++;
            //Check for new targets every second to avoid excess calls
            if (searchTimer >= 20)
            {
                searchTimer = 0;
                AxisAlignedBB bounds = host.getBoundingBox().expand(getTargetDistance(), getTargetDistance(), getTargetDistance());
                List<Entity> list = world().getEntitiesWithinAABBExcludingEntity(entity(), bounds, selector);
                for (Entity entity : list)
                {
                    if (entity instanceof EntityLivingBase && canEasilyReach(entity))
                    {
                        entity().setAttackTarget((EntityLivingBase) entity);
                        return;
                    }
                }
            }
        }
    }

    /**
     * A method used to see if an entity is a suitable target through a number of checks.
     */
    protected boolean isSuitableTarget(EntityLivingBase entity)
    {
        return entity != null && entity.isEntityAlive() && selector != null && selector.isEntityApplicable(entity);
    }

    /**
     * Checks to see if this entity can find a short path to the given target.
     */
    private boolean canEasilyReach(Entity target)
    {
        //TODO replace pathfinder
        PathEntity pathentity = entity().getNavigator().getPathToEntityLiving(target);

        if (pathentity != null)
        {
            PathPoint pathpoint = pathentity.getFinalPathPoint();

            if (pathpoint != null)
            {
                int i = pathpoint.xCoord - MathHelper.floor_double(target.posX);
                int j = pathpoint.zCoord - MathHelper.floor_double(target.posZ);
                return (double) (i * i + j * j) <= 2.25D; //TODO check math on this, might need to adjust for units with long attack ranges
            }
        }
        return false;
    }
}