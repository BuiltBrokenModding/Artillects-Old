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

import java.util.Collections;
import java.util.List;

/**
 * AI task used to find new targets for attacking.
 */
public class AITaskFindTarget<E extends EntityArtillect> extends AITask<E>
{
    /** Timer between search calls */
    private int searchTimer = 0;
    private IEntitySelector selector;

    public AITaskFindTarget(E host, IEntitySelector selector)
    {
        super(host);
        this.setSelector(selector);
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
        if (entity().getAttackTarget() != null && (!entity().getAttackTarget().isEntityAlive() || !isSuitableTarget(entity().getAttackTarget())))
        {
            entity().setAttackTarget(null);
        }
        //TODO keep track of who has what target, so target sharing is minimized to avoid several guards attacking one slime that is harmless
        if (entity().getAttackTarget() == null && ++searchTimer >= searchDelay())
        {
            searchTimer = 0;
            AxisAlignedBB bounds = host.getBoundingBox().expand(getTargetDistance(), getTargetDistance(), getTargetDistance());
            List<Entity> list = world().getEntitiesWithinAABBExcludingEntity(entity(), bounds, getSelector());
            Collections.sort(list, new EntityDistanceSorter(this, true));
            for (Entity entity : list)
            {
                if (entity instanceof EntityLivingBase && canEasilyReach(entity) && host.canEntityBeSeen(entity))
                {
                    entity().setAttackTarget((EntityLivingBase) entity);
                    return;
                }
            }
        }
    }

    /**
     * Amount of time to wait between target search checks
     *
     * @return time in ticks
     */
    protected int searchDelay()
    {
        return 20;
    }

    /**
     * A method used to see if an entity is a suitable target through a number of checks.
     */
    protected boolean isSuitableTarget(EntityLivingBase entity)
    {
        return entity != null && entity.isEntityAlive() && getSelector() != null && getSelector().isEntityApplicable(entity);
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

    /** Selector to validate targets */
    public IEntitySelector getSelector()
    {
        return selector;
    }

    public void setSelector(IEntitySelector selector)
    {
        this.selector = selector;
    }
}