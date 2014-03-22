package artillects.drone.entity.workers;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.drone.entity.EntityArtillectGround;
import artillects.drone.entity.EnumArtillectType;
import artillects.drone.entity.ai.EntityAIArtillectFollow;
import artillects.drone.entity.ai.combat.EntityAIRangedAttack;
import artillects.drone.entity.ai.work.EntityAIBuilding;
import artillects.drone.entity.ai.work.EntityAIReproduce;

/** Drone designed to repair and build structure peaces for the hive
 * 
 * @author DarkGuardsman */
public class EntityFabricator extends EntityArtillectDrone
{
    public EntityFabricator(World par1World)
    {
        super(par1World);
        this.tasks.addTask(1, new EntityAIRangedAttack(this, 1.0D, 5, 10, 30.0F));
        this.tasks.addTask(2, new EntityAIReproduce(this, 0.5f));
        this.tasks.addTask(2, new EntityAIBuilding(this, 0.5f));
        this.tasks.addTask(3, new EntityAIArtillectFollow(this, this.moveForward, 3, 100));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.setSize(0.6f, 0.5f);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(EntityArtillectGround.DATA_TYPE_ID, (byte) EnumArtillectType.FABRICATOR.ordinal());
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        this.cachedInventory = null;
    }
}
