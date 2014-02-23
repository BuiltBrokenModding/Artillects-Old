package com.builtbroken.ai.combat;

import net.minecraft.inventory.IInventory;
import universalelectricity.api.vector.VectorWorld;

public class WeaponProjectile extends WeaponSystemRanged
{
    public WeaponProjectile(IWeaponPlatform shooter, IInventory inv)
    {
        super(shooter, inv);
    }

    @Override
    public void doAttack(VectorWorld target)
    {

    }
}
