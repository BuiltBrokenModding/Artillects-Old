package com.builtbroken.ai.combat;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.DamageSource;
import universalelectricity.api.vector.VectorWorld;

public class WeaponSystemRanged extends WeaponSystem
{
    protected int minRange = 0, maxRange = 64;
    protected DamageSource damageSource;

    public WeaponSystemRanged(IWeaponPlatform shooter, IInventory inv)
    {
        super(shooter, inv);
    }

    @Override
    public void doAttack(VectorWorld target)
    {
    
    }

    @Override
    public void doAttack(Entity entity)
    {
        this.doAttack(VectorWorld.fromCenter(entity));
    }

}
