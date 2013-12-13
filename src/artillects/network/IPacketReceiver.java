package artillects.network;

import net.minecraft.entity.player.EntityPlayer;
import artillects.network.PacketHandler.PacketType;

/**
 * @author Calclavia
 * 
 */
public interface IPacketReceiver
{
	public void onReceivePacket(PacketType container, EntityPlayer player);
}
