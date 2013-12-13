package artillects.entity;

import artillects.hive.Hive;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public abstract class EntityArtillectBase extends EntityCreature implements IDrone
{
    protected int armorSetting = 5;
    /** Owner of the drone either hive or player */
    protected Object owner;

    public EntityArtillectBase(World world)
    {
        super(world);
        Hive.instance().addDrone(this);
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
        return Item.ingotIron.itemID;
    }

    @Override
    protected void dropRareDrop(int par1)
    {
        //TODO small chance to drop intact parts
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

            //TODO spawn zombie drone, eg borg
            EntityZombie entityzombie = new EntityZombie(this.worldObj);
            entityzombie.copyLocationAndAnglesFrom(entityKilled);
            this.worldObj.removeEntity(entityKilled);

            this.worldObj.spawnEntityInWorld(entityzombie);
            this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
        }
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        //TODO if player owned open GUI
        return super.interact(par1EntityPlayer);
    }

    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    public Object getOwner()
    {
        return this.owner;
    }
}
