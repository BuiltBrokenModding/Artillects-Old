package com.builtbroken.ai.combat;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import universalelectricity.api.vector.VectorWorld;

/** Modular way of dealing with weapon systems in a way works with different object types
 * 
 * @author DarkGuardsman */
public class WeaponSystem implements IWeaponSystem
{
    /** Inventory to pull ammo from */
    protected IInventory inventory;
    /** Object that will shoot the weapon */
    protected IWeaponPlatform shooter;

    /** @param shooter - object that will be shooting the weapon
     * @param inv - inventory to pull ammo from */
    public WeaponSystem(IWeaponPlatform shooter, IInventory inv)
    {
        this.shooter = shooter;
        this.inventory = inv;
    }

    @Override
    public boolean canActivate()
    {
        return true;
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

    @Override
    public IWeaponPlatform getWeaponHolder()
    {
        return this.shooter;
    }

    @Override
    public void renderShot(VectorWorld target)
    {
        // TODO Auto-generated method stub

    }
}
