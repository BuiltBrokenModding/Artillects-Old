package artillects.entity.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPlasma extends EntityThrowable {

	private int lifetime = 0;
	
	public EntityPlasma(World par1World, EntityLivingBase par2EntityLivingBase) {
		super(par1World, par2EntityLivingBase);
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		this.worldObj.createExplosion(this, posX, posY, posZ, 1, false);
		this.setDead();
	}
}
