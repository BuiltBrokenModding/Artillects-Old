package com.builtbroken.artillects.content.npc;

import com.builtbroken.artillects.core.entity.ai.AITaskSwimming;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/2/2016.
 */
public class EntityWorkerTest extends EntityNpc
{
    public EntityWorkerTest(World world)
    {
        super(world);
        this.tasks.add(0, new AITaskSwimming(this));
        this.setSize(0.6F, 1.8F);
    }

    public static EntityWorkerTest newEntity(World world)
    {
        EntityWorkerTest entity = new EntityWorkerTest(world);
        entity.getInventory().setChestArmor(new ItemStack(Items.leather_chestplate));
        entity.getInventory().setHeldItem(new ItemStack(Items.iron_axe));
        return entity;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }
}
