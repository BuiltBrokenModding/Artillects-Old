package artillects.content.blocks.teleporter;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.lib.transform.vector.VectorWorld;

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
        if (this.getFrequency() > -1)
        {
            TileEntityTeleporterAnchor teleporter = TeleportManager.getClosestWithFrequency(new VectorWorld(this), this.getFrequency(), this);
            if (teleporter != null)
            {
                TeleportManager.moveEntity(entity, new VectorWorld(teleporter).add(0.5, 2, 0.5));
            }
        }
    }

    /** @return -1 if the teleporter is unable to teleport. */
    public int getFrequency()
    {
        if (System.currentTimeMillis() - this.lastFrequencyCheck > 10)
        {
            this.lastFrequencyCheck = System.currentTimeMillis();
            this.frequency = -1;
            int s = 0;
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                VectorWorld pos = (VectorWorld) new VectorWorld(this).add(direction);

                Block block = pos.getBlock();

                if (block != null && block.getUnlocalizedName().contains("glyph"))
                {
                    s++;
                    int metadata = this.worldObj.getBlockMetadata((int) pos.x(), (int) pos.y(), (int) pos.z());
                    this.frequency += direction.ordinal() * metadata;
                }
            }
            if(s < 4)
            {
                this.frequency = -1;
            }
        }
        return this.frequency;
    }
}
