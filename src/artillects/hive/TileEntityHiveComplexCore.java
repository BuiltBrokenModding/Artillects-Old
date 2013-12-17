package artillects.hive;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.WeightedRandomMinecart;
import net.minecraft.world.World;
import artillects.VectorWorld;
import artillects.entity.EntityFabricator;
import artillects.tile.TileEntityAdvanced;
import cpw.mods.fml.common.registry.EntityRegistry;

public class TileEntityHiveComplexCore extends TileEntityAdvanced
{
    protected HiveComplex complex;
    protected String complexName;
    private final MobSpawnerBaseLogic spawnLogic = new TileEntityMobSpawnerLogic(this);

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (complex == null)
        {
            complex = HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), 30);
            if (complex == null)
            {
                complex = new HiveComplex(this);
            }
        }
        if (complex != null)
        {
            complex.updateEntity();
            if (complex.core != this)
            {
                complex.updateTileLink(this);
            }
        }
        try
        {
            spawnLogic.setMobID(EntityRegistry.instance().lookupModSpawn(EntityFabricator.class, false).getEntityName());
            spawnLogic.updateSpawner();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        //complexName = nbt.getString("complexName");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //nbt.setString("complexName", this.complexName);
    }

    public static class TileEntityMobSpawnerLogic extends MobSpawnerBaseLogic
    {
        /** The mob spawner we deal with */
        final TileEntityHiveComplexCore mobSpawnerEntity;

        TileEntityMobSpawnerLogic(TileEntityHiveComplexCore tileEntityHiveComplexCore)
        {
            this.mobSpawnerEntity = tileEntityHiveComplexCore;
        }

        public void func_98267_a(int par1)
        {
            this.mobSpawnerEntity.worldObj.addBlockEvent(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord, Block.mobSpawner.blockID, par1, 0);
        }

        public World getSpawnerWorld()
        {
            return this.mobSpawnerEntity.worldObj;
        }

        public int getSpawnerX()
        {
            return this.mobSpawnerEntity.xCoord;
        }

        public int getSpawnerY()
        {
            return this.mobSpawnerEntity.yCoord;
        }

        public int getSpawnerZ()
        {
            return this.mobSpawnerEntity.zCoord;
        }

        public Entity func_98265_a(Entity par1Entity)
        {
            Entity entity = super.func_98265_a(par1Entity);
            if (entity instanceof EntityFabricator)
            {
                ((EntityFabricator) entity).setOwner(HiveComplexManager.instance().getClosestComplex(new VectorWorld(par1Entity), 1000));
            }
            return entity;
        }

        public void setRandomMinecart(WeightedRandomMinecart par1WeightedRandomMinecart)
        {
            super.setRandomMinecart(par1WeightedRandomMinecart);

            if (this.getSpawnerWorld() != null)
            {
                this.getSpawnerWorld().markBlockForUpdate(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord);
            }
        }
    }
}
