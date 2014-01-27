package com.builtbroken.ai.combat;

import net.minecraft.entity.Entity;
import universalelectricity.api.vector.VectorWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Applied to objects that are weapons in nature
 * 
 * @author Darkguardsman */
public interface IWeaponSystem
{
    public IWeaponPlatform getWeaponHolder();

    public boolean canActivate();

    public void doAttack(VectorWorld target);

    public void doAttack(Entity entity);
    
    @SideOnly(Side.CLIENT)
    public void renderShot(VectorWorld target);
}
