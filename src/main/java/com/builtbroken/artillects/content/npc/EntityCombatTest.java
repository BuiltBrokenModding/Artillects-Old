package com.builtbroken.artillects.content.npc;

import com.builtbroken.artillects.core.entity.ai.AITaskSwimming;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskFindTarget;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMeleeAttack;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMoveTowardsTarget;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.mc.prefab.entity.selector.EntityLivingSelector;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/2/2016.
 */
public class EntityCombatTest extends EntityNpc
{
    public EntityCombatTest(World world)
    {
        super(world);
        this.tasks.add(0, new AITaskSwimming(this));
        this.tasks.add(2, new AITaskFindTarget(this, new EntityLivingSelector().selectMobs()));
        this.tasks.add(3, new AITaskMoveTowardsTarget(this, 1.0f));
        this.tasks.add(4, new AITaskMeleeAttack(this));
        this.setSize(0.6F, 1.8F);
    }

    public static EntityCombatTest newEntity(World world)
    {
        EntityCombatTest entity = new EntityCombatTest(world);
        entity.getInventory().setHelmArmor(new ItemStack(Items.iron_helmet));
        entity.getInventory().setChestArmor(new ItemStack(Items.leather_chestplate));
        entity.getInventory().setFootArmor(new ItemStack(Items.leather_boots));
        entity.getInventory().setHeldItem(new ItemStack(Items.iron_sword));
        return entity;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }
}
