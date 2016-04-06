package com.builtbroken.artillects.content.npc;

import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionGuard;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Just a test object used to simplify processes needed to check if code works. The actual combat NPC will be created using
 * the EntityNpc class with the profession id of guard or soldier.
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/2/2016.
 */
public class EntityCombatTest extends EntityNpc
{
    public EntityCombatTest(World world)
    {
        super(world);
        this.professionID = "guard";
    }

    public static EntityCombatTest newEntity(World world)
    {
        EntityCombatTest entity = new EntityCombatTest(world);
        entity.getInventory().setHelmArmor(new ItemStack(Items.iron_helmet));
        entity.getInventory().setChestArmor(new ItemStack(Items.leather_chestplate));
        entity.getInventory().setFootArmor(new ItemStack(Items.leather_boots));
        entity.getInventory().setHeldItem(new ItemStack(Items.iron_sword));

        if(entity.profession == null)
        {
            entity.profession = new ProfessionGuard(entity);
        }

        if(entity.profession instanceof ProfessionGuard)
        {
            ((ProfessionGuard) entity.profession).centerOfGuardZone = new Pos(entity);
        }
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
