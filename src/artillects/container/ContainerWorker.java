package artillects.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import artillects.entity.EntityWorker;

/**
 * @author Calclavia
 * 
 */
public class ContainerWorker extends ContainerBase
{
	private EntityPlayer player;

	public ContainerWorker(EntityWorker worker, EntityPlayer player)
	{
		super(player.inventory);
		this.player = player;

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.addSlotToContainer(new Slot(worker.inventory, i + j * 3, 111 + i * 18, -4 + j * 18));
			}
		}

		this.addPlayerInventory(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
}
