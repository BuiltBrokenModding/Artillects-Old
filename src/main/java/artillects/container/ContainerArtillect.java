package artillects.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import artillects.entity.IArtillect;

/**
 * @author Calclavia
 * 
 */
public class ContainerArtillect extends ContainerBase
{
	private EntityPlayer player;

	public ContainerArtillect(IArtillect artillect, EntityPlayer player)
	{
		super(artillect.getInventory());

		this.player = player;

		for (int j = 0; j < 3; j++)
		{
			for (int i = 0; i < 3; i++)
			{
				this.addSlotToContainer(new Slot(artillect.getInventory(), i + j * 3, 111 + i * 18, -4 + j * 18));
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
