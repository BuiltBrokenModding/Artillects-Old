package artillects.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.InventoryHelper;
import artillects.Vector3;
import artillects.entity.ai.EntityAIBlacksmith;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ZoneMining;
import artillects.hive.ZoneProcessing;
import artillects.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityWorker extends EntityArtillectBase implements IPacketReceiver
{
	public enum EnumWorkerType
	{
		HARVESTER, BLACKSMITH;
	}

	public EntityWorker(World par1World)
	{
		super(par1World);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIMining(this, 1));
		this.tasks.addTask(1, new EntityAIBlacksmith(this, 1));
		this.tasks.addTask(2, new EntityAIWander(this, 0.5f));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(EntityArtillectBase.DATA_TYPE_ID, (byte) EnumWorkerType.HARVESTER.ordinal());
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if (this.getZone() == null)
		{
			if (this.getType() == EnumWorkerType.HARVESTER)
			{
				this.setZone(new ZoneMining(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
			else
			{
				this.setZone(new ZoneProcessing(this.worldObj, new Vector3(this).add(-25), new Vector3(this).add(25)));
			}
		}

		this.cachedInventory = null;
	}

	public EnumWorkerType getType()
	{
		return EnumWorkerType.values()[this.getDataWatcher().getWatchableObjectByte(EntityArtillectBase.DATA_TYPE_ID)];
	}

	public void setType(EnumWorkerType type)
	{
		if (this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToServer(Artillects.PACKET_ENTITY.getPacket(this, (byte) type.ordinal()));
		}
		else
		{
			this.getDataWatcher().updateObject(EntityArtillectBase.DATA_TYPE_ID, (byte) (type.ordinal()));
		}
	}

	@Override
	public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		// TODO: REMOVE THIS;
		this.setZone(null);
		this.setType(EnumWorkerType.values()[data.readByte()]);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("type", (byte) this.getType().ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		this.setType(EnumWorkerType.values()[nbt.getByte("type")]);
	}
}
