package artillects.entity.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import artillects.entity.EntityArtillectFlying;

public class EntitySeeker extends EntityArtillectFlying
{
	private EntityLivingBase targetedEntity;

	public int attackCounter;
	protected int targetingRange = 100;

	public EntitySeeker(World world)
	{
		super(world);
		this.setSize(0.7f, 0.3f);
	}

	@Override
	protected void updateEntityActionState()
	{
		super.updateEntityActionState();
		if (this.targetedEntity != null && this.targetedEntity.isDead)
		{
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null)
		{
			this.targetedEntity = this.getTarget(targetingRange);
		}

		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < targetingRange * targetingRange)
		{

			if (this.canEntityBeSeen(this.targetedEntity))
			{
				++this.attackCounter;

				if (this.attackCounter >= 20)
				{
					this.launchFireBall(this.targetedEntity, 2, 1);
					this.attackCounter = -40;
				}
			}
			else if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}
		else
		{
			this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI;

			if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}
	}

}
