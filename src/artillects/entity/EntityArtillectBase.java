package artillects.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
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
import artillects.hive.Hive;
import artillects.hive.Zone;

public abstract class EntityArtillectBase extends EntityCreature implements IArtillect
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
		Hive.instance().addDrone((IArtillect) this);
	}

	@Override
	public void setDead()
	{
		Hive.instance().removeDrone(this);
		super.setDead();
	}

	@Override
	public boolean interact(EntityPlayer entityPlayer)
	{
		entityPlayer.openGui(Artillects.INSTANCE, GuiIDs.ARTILLECT_ENTITY.ordinal(), this.worldObj, this.entityId, 0, 0);
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
			this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
		}
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public void setOwner(EntityPlayer player)
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

	/**
	 * INVENTORY FUNCTIONS
	 * 
	 */
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
		for (int i = 0; i < this.inventory.getSizeInventory(); i++)
		{
			if (itemStack.isItemEqual(this.inventory.getStackInSlot(i)))
			{
				this.inventory.decrStackSize(i, itemStack.stackSize);
				return true;
			}
		}

		return false;
	}

	public boolean tryToWalkNextTo(Vector3 position, double moveSpeed)
	{
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			PathEntity path = this.getNavigator().getPathToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ);

			if (path != null)
			{
				this.getNavigator().tryMoveToXYZ(position.x + direction.offsetX, position.y + direction.offsetY, position.z + direction.offsetZ, moveSpeed);
				return true;
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

		// TODO: Save owner.
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
	}
}
