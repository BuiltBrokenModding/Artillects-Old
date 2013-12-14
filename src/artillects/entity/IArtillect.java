package artillects.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface IArtillect
{
	public void setOwner(EntityPlayer player);

	public Object getOwner();
}
