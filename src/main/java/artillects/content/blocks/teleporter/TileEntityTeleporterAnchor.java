package artillects.content.blocks.teleporter;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.prefab.tile.TileAdvanced;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

/** @author Archadia */
public class TileEntityTeleporterAnchor extends TileEntity
{
    private long lastFrequencyCheck = 0;
    private int frequency = 0;

    @Override
    public void validate()
    {
        if (!worldObj.isRemote)
        {
            TeleportManager.addAnchor(this);
        }

        super.validate();
    }

    @Override
    public void invalidate()
    {
        if (!worldObj.isRemote)
        {
            TeleportManager.remAnchor(this);
        }

        super.invalidate();
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public void doTeleport(Entity entity)
    {
        if (this.getFrequency() > 0)
        {
            TileEntityTeleporterAnchor teleporter = TeleportManager.getClosestWithFrequency(new VectorWorld(this), this.getFrequency(), this);
            if (teleporter != null)
            {
                TeleportManager.moveEntity(entity, new VectorWorld(teleporter).translate(0.5, 2, 0.5));
            }
        }
    }

    /** @return -1 if the teleporter is unable to teleport. */
    public int getFrequency()
    {
        if (System.currentTimeMillis() - this.lastFrequencyCheck > 10)
        {
            this.lastFrequencyCheck = System.currentTimeMillis();
            this.frequency = 0;
            for (int i = 2; i < 6; i++)
            {
                ForgeDirection direction = ForgeDirection.getOrientation(i);
                Vector3 position = new Vector3(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);

                Block block = Block.blocksList[this.worldObj.getBlockId((int) position.x, (int) position.y, (int) position.z)];

                if (block != null && block.getUnlocalizedName().contains("glyph"))
                {
                    int metadata = this.worldObj.getBlockMetadata((int) position.x, (int) position.y, (int) position.z);
                    this.frequency += Math.pow(4, i - 2) * metadata;
                }
                else
                {
                    return -1;
                }
            }
        }
        return this.frequency;
    }
}
