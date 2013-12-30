package artillects.entity.combat;

import artillects.entity.ai.combat.EntityAIRangedAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityCombatDrone extends EntityDemolisher
{
    public EntityCombatDrone(World par1World)
    {
        super(par1World);
    }

}
