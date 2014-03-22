package artillects.drone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import artillects.drone.container.ContainerArtillect;
import artillects.drone.entity.workers.EntityWorker;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public static enum GuiIDs
	{
		ARTILLECT_ENTITY;
	}

	public void preInit()
	{

	}

	public void init()
	{

	}

	public void postInit()
	{

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == GuiIDs.ARTILLECT_ENTITY.ordinal())
		{
			Entity entity = world.getEntityByID(x);

			if (entity instanceof EntityWorker)
			{
				return new ContainerArtillect((EntityWorker) entity, player);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	public void renderLaser(World world, Vector3 start, Vector3 end, float r, float g, float b)
	{

	}

}
