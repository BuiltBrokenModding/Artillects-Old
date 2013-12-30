package artillects.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.hive.ArtillectType;
import artillects.hive.HiveComplex;
import artillects.hive.HiveComplexManager;

/** Prefab for ground based drones
 * 
 * @author DarkGuardsman */
public abstract class EntityArtillectGround extends EntityArtillectBase
{
  
    public static long[] lastAudioPlay = new long[5];

    protected int armorSetting = 5;

    public static final int DATA_TYPE_ID = 12;

    public static final int interactionDistance = 2;

    public EntityArtillectGround(World world)
    {
        super(world);
        this.setSize(1, 1);
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z)
    {
        Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];
        Block blockAbove = Block.blocksList[this.worldObj.getBlockId(x, y + 1, z)];
        if (block == Block.fire || blockAbove == Block.fire || block == Block.lavaMoving || blockAbove == Block.lavaStill)
        {
            return -1000;
        }
        if (block == Artillects.blockLight || block == Artillects.blockWall1 || block == Artillects.blockWall2)
        {
            return 1000;
        }
        return 0.5F + this.worldObj.getLightBrightness(x, y, z);
    }

    @Override
    protected String getLivingSound()
    {
        if (System.currentTimeMillis() - this.lastAudioPlay[0] > 100)
        {
            this.lastAudioPlay[0] = System.currentTimeMillis();
            return Artillects.PREFIX + "voice-random";
        }

        return null;
    }

    @Override
    protected String getDeathSound()
    {
        return Artillects.PREFIX + "voice-lost";
    }

  

    @Override
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
    {
        super.setAttackTarget(par1EntityLivingBase);
        if (System.currentTimeMillis() - this.lastAudioPlay[1] > 100)
        {
            this.lastAudioPlay[1] = System.currentTimeMillis();
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + "voice-target", 1, 1);
        }
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {
        if (System.currentTimeMillis() - this.lastAudioPlay[2] > 100)
        {
            this.lastAudioPlay[2] = System.currentTimeMillis();
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + "voice-welcome", 1, 1);
        }

        if (this.getOwner() instanceof HiveComplex && ((HiveComplex) this.getOwner()).playerZone)
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
        return false;
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
    public void setLastAttacker(Entity par1Entity)
    {
        // TODO: This doesn't get called properly.
        super.setLastAttacker(par1Entity);

        if (this.ticksExisted - this.getLastAttackerTime() >= 60)
        {
            if (System.currentTimeMillis() - this.lastAudioPlay[3] > 100)
            {
                this.lastAudioPlay[3] = System.currentTimeMillis();
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + "voice-firstSight", 1, 1);
            }
        }
    }

    @Override
    public void onKillEntity(EntityLivingBase entityKilled)
    {
        super.onKillEntity(entityKilled);
        if (System.currentTimeMillis() - this.lastAudioPlay[4] > 100)
        {
            this.lastAudioPlay[4] = System.currentTimeMillis();
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, Artillects.PREFIX + "voice-kill", 1, 1);
        }
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
        nbt.setByte("type", (byte) this.getType().ordinal());
        nbt.setBoolean("hive", !(this.getOwner() instanceof HiveComplex && ((HiveComplex) this.getOwner()).playerZone));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setType(ArtillectType.values()[nbt.getByte("type")]);
        if (!nbt.getBoolean("hive"))
        {
            HiveComplex.getPlayerHive().addDrone(this);
        }
        else
        {
            HiveComplex hive = HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), 1000);
            if (hive != null)
            {
                hive.addDrone(this);
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3(this).translate(0, 0.2, 0), new Vector3(entity).translate(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

    }

}
