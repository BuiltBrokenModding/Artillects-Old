package artillects.entity.workers;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import artillects.entity.EntityArtillectGround;
import artillects.entity.ai.combat.EntityAIRangedAttack;
import artillects.entity.ai.work.EntityAIBlacksmith;
import artillects.entity.ai.work.EntityAICrafting;
import artillects.entity.ai.work.EntityAIMining;
import artillects.hive.EnumArtillectType;
import artillects.hive.complex.HiveComplex;
import artillects.hive.zone.Zone;
import artillects.hive.zone.ZoneMining;
import artillects.hive.zone.ZoneProcessing;

/** Drone designed to do generic work for the hive including mining, harvesting, collection, sorting,
 * crafting, processing, and even combat.
 * 
 * @author DarkGuardsman */
public class EntityWorker extends EntityArtillectDrone
{
    public EntityWorker(World par1World)
    {
        super(par1World);
        this.tasks.addTask(1, new EntityAIRangedAttack(this, 1.0D, 5, 10, 30.0F));
        this.tasks.addTask(2, new EntityAIMining(this, 1));
        this.tasks.addTask(2, new EntityAIBlacksmith(this, 1));
        this.tasks.addTask(2, new EntityAICrafting(this, 1));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(EntityArtillectGround.DATA_TYPE_ID, (byte) EnumArtillectType.HARVESTER.ordinal());
    }

    @Override
    public void setType(EnumArtillectType type)
    {
        super.setType(type);
        genZone();
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (this.getHomePosition().posX == 0 && this.getHomePosition().posY == 0 && this.getHomePosition().posZ == 0)
        {
            this.setHomeArea((int) posX, (int) posY, (int) posZ, 100);
        }
        if (!this.worldObj.isRemote && this.getOwner() instanceof HiveComplex && ((HiveComplex) this.getOwner()).playerZone)
        {
            HiveComplex.getPlayerHive().updateEntity();
            HiveComplex.getPlayerHive().addDrone(this);
            if (this.getZone() == null)
            {
                genZone();
            }
        }
        this.cachedInventory = null;
    }

    @Override
    public void setZone(Zone zone)
    {
        if (!this.worldObj.isRemote && this.getOwner() instanceof HiveComplex && ((HiveComplex) this.getOwner()).playerZone)
        {
            if (zone != null)
                HiveComplex.getPlayerHive().addZone(zone);
        }
    }

    public void genZone()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.getZone() == null)
            {
                if (this.getType() == EnumArtillectType.HARVESTER)
                {
                    this.setZone(new ZoneMining(this.worldObj, new Vector3(this.getHomePosition().posX - 25, this.getHomePosition().posY - 10, this.getHomePosition().posZ - 25), new Vector3(this.getHomePosition().posX + 25, this.getHomePosition().posY + 10, this.getHomePosition().posZ + 25)));
                }
                if (this.getType() == EnumArtillectType.BLACKSMITH || this.getType() == EnumArtillectType.CRAFTER)
                {
                    this.setZone(new ZoneProcessing(this.worldObj, new Vector3(this.getHomePosition().posX - 25, this.getHomePosition().posY - 10, this.getHomePosition().posZ - 25), new Vector3(this.getHomePosition().posX + 25, this.getHomePosition().posY + 10, this.getHomePosition().posZ + 25)));
                }
            }
        }
    }
}
