package artillects.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.Vector3;
import artillects.VectorWorld;
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
    private Zone assignedZone;
    private Object owner;
    private boolean isPlayerOwned;

    public EntityArtillectBase(World world)
    {
        super(world);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
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
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (super.attackEntityFrom(source, damage))
        {
            Entity attacker = source.getEntity();

            if (this.riddenByEntity != attacker && this.ridingEntity != attacker)
            {
                if (attacker != this)
                {
                    this.entityToAttack = attacker;
                }

                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public void setDead()
    {
        if (this.getOwner() instanceof HiveComplex)
        {
            ((HiveComplex) this.getOwner()).removeDrone(this);
        }
        super.setDead();
    }

    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public void setOwner(Object hive)
    {
        this.owner = hive;
    }

    @Override
    public Object getOwner()
    {
        if (this.owner == null)
        {
            if (this.isPlayerOwned)
            {
                this.setOwner(HiveComplex.getPlayerHive());
            }
            else
            {
                this.setOwner(HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), -1));
            }
        }
        return this.owner;
    }

    @Override
    public Zone getZone()
    {
        return this.assignedZone;
    }

    @Override
    public void setZone(Zone zone)
    {
        this.assignedZone = zone;
    }

    @Override
    public ArtillectType getType()
    {
        return ArtillectType.get(this.getDataWatcher().getWatchableObjectByte(EntityArtillectGround.DATA_TYPE_ID));
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
            ArtillectType t = this.getType();
            if (t != type)
            {
                this.getDataWatcher().updateObject(EntityArtillectGround.DATA_TYPE_ID, (byte) (type.ordinal()));
                this.setZone(null);
            }
        }
    }

    @Override
    public IInventory getInventory()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setByte("type", (byte) this.getType().ordinal());
        nbt.setBoolean("playerOwned", this.isPlayerOwned);
        if (this.getOwner() instanceof HiveComplex)
        {
            nbt.setString("hiveID", ((HiveComplex) this.getOwner()).getName());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setType(ArtillectType.values()[nbt.getByte("type")]);
        this.isPlayerOwned = nbt.getBoolean("playerOwned");
        if (nbt.hasKey("hiveID") && HiveComplexManager.instance().getHive(nbt.getString("hiveID")) != null)
        {
            this.setOwner(HiveComplexManager.instance().getHive(nbt.getString("hiveID")));
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3(this).add(0, 0.2, 0), new Vector3(entity).add(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

    }

    @Override
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player)
    {
        this.setType(ArtillectType.values()[data.readByte()]);
    }

    @Override
    public boolean canBreatheUnderwater()
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
    public boolean getCanSpawnHere()
    {
        return HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), 200) != null && super.getCanSpawnHere();
    }

    @Override
    public boolean isOnSameTeam(EntityLivingBase entity)
    {
        if (entity instanceof IArtillect && ((IArtillect) entity).getOwner() instanceof HiveComplex)
        {
            if (((HiveComplex) ((IArtillect) entity).getOwner()) == this.getOwner())
            {
                return true;
            }
        }
        if (entity instanceof EntityPlayer)
        {
            if (this.getOwner() instanceof HiveComplex)
            {
                //TODO when access list is added to hive check for username
            }
        }
        return this.isOnTeam(entity.getTeam());
    }

}
