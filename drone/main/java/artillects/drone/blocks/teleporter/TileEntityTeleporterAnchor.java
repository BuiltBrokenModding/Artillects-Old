package artillects.drone.blocks.teleporter;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.prefab.tile.TileAdvanced;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

/** @author Archadia */
public class TileEntityTeleporterAnchor extends TileAdvanced
{
    private long lastFrequencyCheck = 0;
    public long lastVoiceActivation = 0;
    private int frequency = 0;
    private boolean forceXYZ = false;
    private Vector3 teleportSpot = null;

    public void setTeleportLocation(Vector3 location)
    {
        if (location != null)
        {
            this.forceXYZ = true;
            this.teleportSpot = location;
        }
    }

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
        VectorWorld teleportSpot = null;
        if (this.forceXYZ && this.teleportSpot != null)
        {
            teleportSpot = new VectorWorld(this.worldObj, this.teleportSpot);
        }
        else if (this.getFrequency() > 0)
        {
            TileEntityTeleporterAnchor teleporter = TeleportManager.getClosestWithFrequency(new VectorWorld(this), this.getFrequency(), this);
            if (teleporter != null)
            {
                teleportSpot = new VectorWorld(teleporter).translate(0.5, 2, 0.5);
            }
        }

        if (teleportSpot != null)
        {
            TeleportManager.moveEntity(entity, teleportSpot);
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

                if (block instanceof BlockGlyph)
                {
                    int metadata = this.worldObj.getBlockMetadata((int) position.x, (int) position.y, (int) position.z);
                    this.frequency += Math.pow(BlockGlyph.MAX_GLYPH, i - 2) * metadata;
                }
                else
                {
                    return -1;
                }
            }
        }
        return this.frequency;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (this.teleportSpot != null)
        {
            nbt.setCompoundTag("teleportLocation", this.teleportSpot.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (nbt.hasKey("teleportLocation"))
        {
            this.teleportSpot = new Vector3(nbt.getCompoundTag("teleportLocation"));
            this.forceXYZ = true;
        }
    }
}
