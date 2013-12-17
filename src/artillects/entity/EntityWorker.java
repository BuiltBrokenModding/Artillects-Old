package artillects.entity;

import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.entity.ai.EntityAIBlacksmith;
import artillects.entity.ai.EntityAICrafting;
import artillects.entity.ai.EntityAIMining;
import artillects.hive.ArtillectType;
import artillects.hive.HiveComplex;
import artillects.hive.zone.ZoneMining;
import artillects.hive.zone.ZoneProcessing;

public class EntityWorker extends EntityArtillectBase
{
    public EntityWorker(World par1World)
    {
        super(par1World);
        this.tasks.addTask(0, new EntityAIMining(this, 1));
        this.tasks.addTask(0, new EntityAIBlacksmith(this, 1));
        this.tasks.addTask(0, new EntityAICrafting(this, 1));
        this.tasks.addTask(1, new EntityAIWander(this, 0.5f));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(EntityArtillectBase.DATA_TYPE_ID, (byte) ArtillectType.HARVESTER.ordinal());
    }

    @Override
    public void setType(ArtillectType type)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.getType() != type)
            {
                genZone();
            }

        }
        super.setType(type);

    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        if (!this.worldObj.isRemote && this.getOwner() instanceof HiveComplex && ((HiveComplex) this.getOwner()).playerZone)
        {
            HiveComplex.getPlayerHive().updateEntity();
            HiveComplex.getPlayerHive().addDrone(this);
            if (this.getZone() == null)
            {
                genZone();
            }
            if (this.getZone() != null)
            {
                this.getZone().updateEntity();
            }
        }
        this.cachedInventory = null;
    }

    public void genZone()
    {
        System.out.println("Creating zone at " + this.getHomePosition().posX + "x " + this.getHomePosition().posY + "y " + this.getHomePosition().posZ + "z ");
        if (this.zone != null)
        {
            if (this.getZone() instanceof ZoneMining && this.getType() == ArtillectType.HARVESTER)
            {
                return;
            }
            if (this.getZone() instanceof ZoneProcessing && this.getType() == ArtillectType.BLACKSMITH)
            {
                return;
            }
        }
        if (this.getType() == ArtillectType.HARVESTER)
        {
            this.setZone(new ZoneMining(HiveComplex.getPlayerHive(), new Vector3(this.getHomePosition().posX - 25, this.getHomePosition().posY - 10, this.getHomePosition().posZ - 25), new Vector3(this.getHomePosition().posX + 25, this.getHomePosition().posY + 10, this.getHomePosition().posZ + 25)));
        }
        if (this.getType() == ArtillectType.BLACKSMITH)
        {
            this.setZone(new ZoneProcessing(HiveComplex.getPlayerHive(), new Vector3(this.getHomePosition().posX - 25, this.getHomePosition().posY - 10, this.getHomePosition().posZ - 25), new Vector3(this.getHomePosition().posX + 25, this.getHomePosition().posY + 10, this.getHomePosition().posZ + 25)));
        }
    }
}
