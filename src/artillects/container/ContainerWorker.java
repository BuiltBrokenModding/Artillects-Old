package artillects.container;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Calclavia
 * 
 */
public class ContainerWorker extends ContainerBase
{
	private EntityPlayer player;

	public ContainerWorker(EntityPlayer player)
	{
		this.player = player;
		this.inventory = this.player.inventory;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
}
