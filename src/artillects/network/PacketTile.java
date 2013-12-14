package artillects.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavia */
public class PacketTile extends PacketType
{

	public Packet getPacket(TileEntity tileEntity, String id, Object... arg)
	{
		return super.getPacket(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, id, arg);
	}

	@Override
	public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		TileEntity tileEntity = player.worldObj.getBlockTileEntity(data.readInt(), data.readInt(), data.readInt());
		final String packetID = data.readUTF();
		if (tileEntity instanceof IPacketReceiver)
		{
			((IPacketReceiver) tileEntity).onReceivePacket(packetID, data, player);
		}
	}
}
