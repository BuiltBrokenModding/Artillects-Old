package artillects.entity;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.world.World;

public class EntityFabricator extends EntityArtillectBase
{
	public EntityFabricator(World par1World)
	{
		super(par1World);
		this.tasks.addTask(0, new EntityAIWander(this, 0.5f));
	}

}
