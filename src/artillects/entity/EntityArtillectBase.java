package artillects.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.InventoryHelper;
import artillects.Vector3;
import artillects.VectorWorld;
import artillects.block.teleporter.TileEntityTeleporterAnchor;
import artillects.hive.ArtillectType;
import artillects.hive.HiveComplex;
import artillects.hive.HiveComplexManager;
import artillects.hive.zone.Zone;
import artillects.item.ItemParts;
import artillects.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

public class EntityArtillectBase extends EntityCreature implements IArtillect, IPacketReceiver, IRangedAttackMob
{
	public InventoryBasic inventory = new InventoryBasic("gui.artillect", false, 9);
	protected List<ItemStack> cachedInventory;

	protected int armorSetting = 5;

	public Zone zone;

	/** Owner of the drone either hive or player */
	public Object owner;

	public static final int DATA_TYPE_ID = 12;

	public static final int interactionDistance = 2;

	public EntityArtillectBase(World world)
	{
		super(world);
		this.setSize(1, 1);
		this.tasks.addTask(5, new EntityAIArrowAttack(this, 1.0D, 20, 100, 15.0F));
		// this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.0D,
		// false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));// TODO remove friendly
	}

	@Override
	public ArtillectType getType()
	{
		return ArtillectType.get(this.getDataWatcher().getWatchableObjectByte(EntityArtillectBase.DATA_TYPE_ID));
	}

	@Override
	public void setType(ArtillectType type)
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
	public IInventory getInventory()
	{
		return this.inventory;
	}

	@Override
	public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player)
	{
		// TODO: REMOVE THIS;
		this.setZone(null);
		this.setType(ArtillectType.values()[data.readByte()]);
	}

	@Override
	public boolean canBreatheUnderwater()
	{
		return true;
	}

	@Override
	public void setDead()
	{
		if (this.getOwner() instanceof HiveComplex)
		{
			((HiveComplex) this.getOwner()).removeDrone(this);
		}
		else if (this.getOwner() instanceof EntityPlayer)
		{
			((EntityPlayer) this.getOwner()).sendChatToPlayer(ChatMessageComponent.createFromText("One of your drones has died"));
		}
		super.setDead();
	}

	@Override
	public boolean interact(EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			this.setType(this.getType().toggle(this));
			entityPlayer.addChatMessage("Toggled to: " + this.getType().name());
		}
		else
		{
			entityPlayer.openGui(Artillects.instance, GuiIDs.ARTILLECT_ENTITY.ordinal(), this.worldObj, this.entityId, 0, 0);
		}
		return true;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(1.0D);
	}

	@Override
	public int getTotalArmorValue()
	{
		return armorSetting;
	}

	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}

	@Override
	protected int getDropItemId()
	{
		// TODO: Drop some kind of circuit
		return Artillects.itemParts.itemID;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		super.dropFewItems(par1, par2);
		if (par1)
		{
			this.entityDropItem(new ItemStack(Artillects.itemParts, 1 + this.worldObj.rand.nextInt(2 + par2), this.worldObj.rand.nextInt(ItemParts.Part.values().length - 1)), 0.0F);
			this.entityDropItem(new ItemStack(Artillects.itemParts, 1 + this.worldObj.rand.nextInt(5 + par2), this.worldObj.rand.nextInt(ItemParts.Part.values().length - 1)), 0.0F);

		}
	}

	@Override
	protected void dropRareDrop(int par1)
	{

	}

	@Override
	public void setLastAttacker(Entity par1Entity)
	{
		// TODO: This doesn't get called properly.
		super.setLastAttacker(par1Entity);

		if (this.ticksExisted - this.getLastAttackerTime() >= 60)
		{
			this.playSoundEffect(0);
		}
	}

	public void playSoundEffect(int id)
	{
		String[] soundNames = new String[] { "voice-agressionDetected", "voice-intruderAlert", "voice-pleaseDisarmYourself", "voice-theHiveIsUnderAttack" };
		this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + soundNames[id], 1f, 1f);
	}

	@Override
	public void onKillEntity(EntityLivingBase entityKilled)
	{
		super.onKillEntity(entityKilled);

		if (this.worldObj.difficultySetting >= 2 && entityKilled instanceof EntityZombie)
		{
			if (this.worldObj.difficultySetting == 2 && this.rand.nextBoolean())
			{
				return;
			}

			// TODO spawn zombie drone, eg borg
			EntityZombie entityzombie = new EntityZombie(this.worldObj);
			entityzombie.copyLocationAndAnglesFrom(entityKilled);
			this.worldObj.removeEntity(entityKilled);

			this.worldObj.spawnEntityInWorld(entityzombie);
		}

		this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + "voice-kill", 1, 1);
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public void setOwner(Object player)
	{
		this.owner = player;
	}

	@Override
	public Object getOwner()
	{
		return this.owner;
	}

	@Override
	public Zone getZone()
	{
		return this.zone;
	}

	@Override
	public void setZone(Zone zone)
	{
		this.zone = zone;
	}

	/** INVENTORY FUNCTIONS */
	/** @return True if the Worker's inventory is full. (See EntityAIMining) */
	public boolean isInventoryFull()
	{
		return InventoryHelper.isInventoryFull(this.inventory);
	}

	public boolean isInventoryEmpty()
	{
		return InventoryHelper.isInventoryEmpty(this.inventory);
	}

	/**
	 * Adds a stack into the inventory.
	 * 
	 * @param stack - The stack to add
	 * @return - The remaining stack.
	 */
	public ItemStack increaseStackSize(ItemStack stack)
	{
		return InventoryHelper.addStackToInventory(this.inventory, stack);
	}

	@Override
	protected void dropEquipment(boolean par1, int par2)
	{
		super.dropEquipment(par1, par2);
		if (inventory != null && !this.worldObj.isRemote)
		{
			for (int i = 0; i < inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = inventory.getStackInSlot(i);

				if (itemstack != null)
				{
					this.entityDropItem(itemstack, 0.0F);
				}
			}
		}
	}

	public List<ItemStack> getInventoryAsList()
	{
		if (this.cachedInventory == null)
		{
			this.cachedInventory = InventoryHelper.getInventoryAsList(this.inventory);
		}

		return this.cachedInventory;
	}

	public boolean hasItem(ItemStack... itemStacks)
	{
		return InventoryHelper.hasItem(this.inventory, itemStacks);
	}

	public boolean decreaseStackSize(ItemStack itemStack)
	{
		return InventoryHelper.decreaseStackSize(this.inventory, itemStack);
	}

	public boolean tryToWalkNextTo(Vector3 position, double moveSpeed)
	{
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			PathEntity path = this.getNavigator().getPathToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ);

			if (path != null)
			{
				return this.getNavigator().tryMoveToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ, moveSpeed);
			}
		}

		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.inventory.getStackInSlot(i);

			if (itemstack != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				itemstack.writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
		nbt.setByte("type", (byte) this.getType().ordinal());
		nbt.setBoolean("hive", !(this.getOwner() != HiveComplex.getPlayerHive()));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		NBTTagList nbttaglist = nbt.getTagList("Items");

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j < this.inventory.getSizeInventory())
			{
				this.inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
			}
		}

		this.setType(ArtillectType.values()[nbt.getByte("type")]);
		if (!nbt.getBoolean("hive"))
		{
			HiveComplex.getPlayerHive().addDrone(this);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
	{
		entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
		Artillects.proxy.renderLaser(this.worldObj, new Vector3(this).add(0, 0.2, 0), new Vector3(entity).add(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

	}

	@Override
	public boolean getCanSpawnHere()
	{
		return HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), 100) != null;
	}
}
