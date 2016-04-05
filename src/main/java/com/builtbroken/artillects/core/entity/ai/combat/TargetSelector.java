package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.artillects.api.IEntity;
import com.builtbroken.artillects.api.IFactionMember;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Target selector geared towards filtering targets based on internal settings.
 */
public class TargetSelector implements IEntitySelector
{
    /** Host currently using this target selector */
    public final IEntity hostEntity;
    /** Target entities implementing {@link IMob} */
    public boolean monsters = true;
    /** Target entities implementing {@link IAnimals} */
    public boolean animals = false;
    /** Target entities implementing {@link INpc} */
    public boolean npcs = false;
    /** Target entities that extend {@link EntityPlayer}, only if difficulty is not easy mode */
    public boolean players = true;
    /** Target entities that extend {@link EntityFlying} */
    public boolean flying = true;

    public TargetSelector(IEntity drone)
    {
        this.hostEntity = drone;
    }

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            if (entity.isEntityAlive() && !entity.isInvisible())
            {
                if (entity instanceof EntityFlying)
                {
                    return flying;
                }
                if (hostEntity instanceof IFactionMember)
                {
                    if (((IFactionMember) hostEntity).getFaction().isMember(entity))
                    {
                        return false;
                    }
                }
                if (players && entity instanceof EntityPlayer && entity.worldObj.difficultySetting.ordinal() > 0)
                {
                    if (!((EntityPlayer) entity).capabilities.isCreativeMode)
                    {
                        return true;
                    }
                }
                if (monsters && entity instanceof IMob)
                {
                    return true;
                }
                if (animals && entity instanceof IAnimals)
                {
                    return true;
                }
                if (npcs && entity instanceof INpc)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
