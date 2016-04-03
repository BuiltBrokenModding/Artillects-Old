package com.builtbroken.artillects.core.entity.passive;

import com.builtbroken.artillects.core.entity.EntityHumanoid;
import net.minecraft.entity.INpc;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Prefab for all NPC that artillects will contain including crafters and fighters
 *
 * @author Darkguardsman
 */
public class EntityNpc extends EntityHumanoid<InventoryNPC> implements INpc
{
    public EntityNpc(World world)
    {
        super(world);
        inventory = new InventoryNPC();
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack)
    {
        //TODO implement
    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return inventory != null ? inventory.gear : null;
    }

    @Override
    public ItemStack getHeldItem()
    {
        return inventory != null ? inventory.gear[0] : null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot)
    {
        return inventory != null && slot >= 0 && slot < 5 ? inventory.gear[slot] : null;
    }
}
