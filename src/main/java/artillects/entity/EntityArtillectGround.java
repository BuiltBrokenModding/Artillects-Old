package artillects.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import artillects.Artillects;
import artillects.CommonProxy.GuiIDs;
import artillects.hive.complex.HiveComplex;

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
        if (block == Artillects.blockLight || block == Artillects.blockHiveWalling)
        {
            return 1000;
        }
        return 0.5F + this.worldObj.getLightBrightness(x, y, z);
    } 

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {

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
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3(this).translate(0, 0.2, 0), new Vector3(entity).translate(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

    }

}
