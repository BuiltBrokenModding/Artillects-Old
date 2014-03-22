package artillects.drone.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import artillects.drone.entity.EntityArtillectGround;

/**
 * @author Calclavia
 * 
 */
public abstract class EntityArtillectAIBase extends EntityAIBase
{
	protected World world;
	protected double moveSpeed;

	/** The last chest used by the Artillect. Saved to be "closed" next tick. */
	protected TileEntityChest lastUseChest;

	public EntityArtillectAIBase(World world, double moveSpeed)
	{
		this.world = world;
		this.moveSpeed = moveSpeed;
	}


	public abstract EntityArtillectGround getArtillect();
}
