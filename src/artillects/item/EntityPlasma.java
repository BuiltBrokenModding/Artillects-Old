package artillects.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPlasma extends Entity implements IProjectile {

	private int lifetime = 0;
	
	public EntityPlasma(World par1World, double posX, double posY, double posZ) {
		super(par1World);
		this.setSize(1F, 1F);
        this.setPosition(posX, posY, posZ);
		this.motionX = 1;
	}

	public void onUpdate() {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if(lifetime > 50) {
			this.worldObj.createExplosion(this, posX, posY, posZ, 2, false);
			this.setDead();
		}
		lifetime += 1;
	}
	
	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

	}

	@Override
	public void setThrowableHeading(double d0, double d1, double d2, float f,
			float f1) {
		
	}

}
