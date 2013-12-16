package artillects.entity;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.ai.EntityAIBlacksmith;
import artillects.entity.ai.EntityAICrafting;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ArtillectType;
import artillects.hive.zone.ZoneMining;
import artillects.hive.zone.ZoneProcessing;

public class EntityWorker extends EntityArtillectBase
{
	public EntityWorker(World par1World)
	{
		super(par1World);
		this.tasks.addTask(0, new EntityAIMining(this, 1));
		this.tasks.addTask(0, new EntityAIBlacksmith(this, 1));
		this.tasks.addTask(0, new EntityAICrafting(this, 1));
		this.tasks.addTask(1, new EntityAIWander(this, 0.5f));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(EntityArtillectBase.DATA_TYPE_ID, (byte) ArtillectType.HARVESTER.ordinal());
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (this.getZone() == null)
		{
			if (this.getType() == ArtillectType.HARVESTER)
			{
				this.setZone(new ZoneMining(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
			else
			{
				this.setZone(new ZoneProcessing(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
		}

		this.cachedInventory = null;
	}
}
