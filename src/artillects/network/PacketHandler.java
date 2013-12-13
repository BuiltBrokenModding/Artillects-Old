package artillects.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import artillects.Artillects;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Handles the packets.
 * 
 * @author Calclavia
 * 
 */
public class PacketHandler implements IPacketHandler
{
	public static final ArrayList<PacketType> registeredPackets = new ArrayList<PacketType>();

	public class PacketTile extends PacketType
	{
		public Packet getPacket(TileEntity tileEntity, Object... arg)
		{
			return super.getPacket(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, arg);
		}

		@Override
		public void receivePacket(ByteArrayDataInput data, EntityPlayer player)
		{
			TileEntity tileEntity = player.worldObj.getBlockTileEntity(data.readInt(), data.readInt(), data.readInt());

			if (tileEntity instanceof IPacketReceiver)
			{
				((IPacketReceiver) tileEntity).onReceivePacket(this, player);
			}
		}
	}

	public abstract class PacketType
	{
		public final int id;

		public PacketType()
		{
			id = registeredPackets.size();
			registeredPackets.add(this);
		}

		public Packet getPacket(Object... arg)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				DataOutputStream data = new DataOutputStream(bytes);

				for (Object obj : arg)
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
						writeNBTTagCompound((NBTTagCompound) obj, data);
					}
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

		public abstract void receivePacket(ByteArrayDataInput data, EntityPlayer player);
	}

	/**
	 * Reads a compressed NBTTagCompound from the InputStream
	 */
	public static NBTTagCompound readNBTTagCompound(DataInput par0DataInput) throws IOException
	{
		short short1 = par0DataInput.readShort();

		if (short1 < 0)
		{
			return null;
		}
		else
		{
			byte[] abyte = new byte[short1];
			par0DataInput.readFully(abyte);
			return CompressedStreamTools.decompress(abyte);
		}
	}

	public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutput.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutput.writeShort((short) abyte.length);
			par1DataOutput.write(abyte);
		}
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

			int packetID = data.readInt();
			EntityPlayer entityPlayer = (EntityPlayer) player;

			registeredPackets.get(packetID).receivePacket(data, entityPlayer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
