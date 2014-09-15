package artillects.drone.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import artillects.core.Artillects;
import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.core.transform.vector.IVector3;
import universalelectricity.core.transform.vector.Vector3;

/** Prefab for ground based drones
 * 
 * @author DarkGuardsman */
public abstract class EntityArtillectGround extends EntityArtillectBase
{
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
        Block block = this.worldObj.getBlock(x, y, z);
        Block blockAbove = this.worldObj.getBlock(x, y + 1, z);
        if (block == Blocks.fire || blockAbove == Blocks.fire || block == Blocks.flowing_lava || blockAbove == Blocks.lava)
        {
            return -1000;
        }
        return 0.5F + this.worldObj.getLightBrightness(x, y, z);
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
            PathEntity path = this.getNavigator().getPathToXYZ(position.x() + direction.offsetX, position.y() + direction.offsetY, position.z() + direction.offsetZ);

            if (path != null)
            {
                return this.getNavigator().tryMoveToXYZ(position.x() + direction.offsetX, position.y() + direction.offsetY, position.z() + direction.offsetZ, moveSpeed);
            }
        }

        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f)
    {
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
        entity.setFire(5);
        Artillects.proxy.renderLaser(this.worldObj, new Vector3((IVector3)this).add(0, 0.2, 0), new Vector3(entity).add(entity.width / 2, entity.height / 2, entity.width / 2), 1, 0, 0);

    }

}
