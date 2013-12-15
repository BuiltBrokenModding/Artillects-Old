package artillects.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import artillects.Artillects;

import com.google.common.io.ByteArrayDataInput;

/** @author Calclavia */
public abstract class PacketType
{
	public final int id;

	public PacketType()
	{
		this.id = PacketHandler.registeredPackets.size();
		PacketHandler.registeredPackets.add(this);
	}

	public Packet getPacket(Object... arg)
	{
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);

			this.writeData(data, this.id);

			for (Object obj : arg)
			{
				this.writeData(data, obj);
			}

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = Artillects.CHANNEL;
			packet.data = bytes.toByteArray();
			packet.length = packet.data.length;

			return packet;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Called to allow for custom data types to be encoded */
	public void writeData(DataOutputStream data, Object obj)
	{
		try
		{
			if (obj instanceof String)
			{
				data.writeUTF((String) obj);
			}
			else if (obj instanceof Integer)
			{
				data.writeInt((Integer) obj);
			}
			else if (obj instanceof Float)
			{
				data.writeFloat((Float) obj);
			}
			else if (obj instanceof Long)
			{
				data.writeLong((Long) obj);
			}
			else if (obj instanceof Short)
			{
				data.writeShort((Short) obj);
			}
			else if (obj instanceof Byte)
			{
				data.writeByte((Byte) obj);
			}
			else if (obj instanceof NBTTagCompound)
			{
				PacketHandler.writeNBTTagCompound((NBTTagCompound) obj, data);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void receivePacket(ByteArrayDataInput data, EntityPlayer player);
}
