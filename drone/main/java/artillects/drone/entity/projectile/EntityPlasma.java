package artillects.drone.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPlasma extends EntityThrowable
{

	private int groundLife = 0;
	boolean impacted = false;
	private int lifetime = 0;

	public EntityPlasma(World par1World, EntityLivingBase par2EntityLivingBase)
	{
		super(par1World, par2EntityLivingBase);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (impacted)
		{
			groundLife++;
			if (groundLife == 30)
			{
				worldObj.createExplosion(this, posX, posY, posZ, 2, false);
				this.setDead();
				impacted = false;
			}
			worldObj.spawnParticle("smoke", posX, posY, posZ, 0, 0, 0);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{

	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		impacted = true;
		motionX = 0;
		motionZ = 0;
		motionY = 0;
	}
}
