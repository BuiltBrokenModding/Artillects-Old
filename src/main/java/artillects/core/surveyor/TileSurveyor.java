package artillects.core.surveyor;

import java.awt.Color;

import artillects.core.Artillects;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.content.module.TileBase;
import resonant.lib.prefab.vector.Cuboid;
import universalelectricity.api.vector.EulerAngle;
import universalelectricity.api.vector.Vector3;

/** Small camera looking block that can deploy laser lines, gauge distances, and do other utilities.
 * 
 * @author Darkguardsman */
public class TileSurveyor extends TileBase
{
    protected EulerAngle angle;
    protected Color beamColor = Color.RED;
    protected Vector3 lastRayHit = null;
    protected Vector3 loc = null;

    public TileSurveyor()
    {
        super(Material.iron);
        angle = new EulerAngle(ForgeDirection.NORTH);
        bounds = new Cuboid(0, 0, 0, 1, 0.9f, 1);
        isOpaqueCube = false;
        //normalRender = false;
        itemBlock = ItemSurveyor.class;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.ticks % 3 == 0)
        {
            lastRayHit = null;
            if (this.loc == null)
                loc = new Vector3(this).translate(0.5, 0.5, 0.5);

            MovingObjectPosition hit = getRayHit();
            if (hit != null && hit.typeOfHit == EnumMovingObjectType.TILE)
            {
                lastRayHit = new Vector3(hit).translate(0.5, 0.5, 0.5);
                System.out.println("Ray hit = " + lastRayHit);
            }
            //TODO render distance above tile
            if (lastRayHit != null && !world().isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
            {
                Artillects.proxy.renderLaser(world(), loc, lastRayHit, beamColor, 3);
            }
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    public MovingObjectPosition getRayHit()
    {
        Vector3 surveyor = new Vector3(this);
        Vector3 destination = new Vector3(angle);
        
        surveyor.add(destination);
        surveyor.translate(0.5, 0.5, 0.5);
        destination.scale(1000);
        destination.add(surveyor);
        
        return surveyor.rayTraceBlocks(world(), destination, true);
    }

    public double distance()
    {
        if (lastRayHit != null)
        {
            return lastRayHit.distance(xCoord, yCoord, zCoord);
        }
        return -1;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        angle.yaw = nbt.getDouble("yaw");
        angle.pitch = nbt.getDouble("pitch");
        if (nbt.hasKey("beamColor"))
        {
            NBTTagCompound tag = nbt.getCompoundTag("beamColor");
            this.beamColor = new Color(tag.getInteger("red"), tag.getInteger("blue"), tag.getInteger("green"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setDouble("yaw", angle.yaw());
        nbt.setDouble("pitch", angle.pitch());
        if (beamColor != Color.RED)
        {
            NBTTagCompound colorBeamTag = new NBTTagCompound();
            colorBeamTag.setInteger("red", beamColor.getRed());
            colorBeamTag.setInteger("blue", beamColor.getBlue());
            colorBeamTag.setInteger("green", beamColor.getGreen());
            nbt.setCompoundTag("beamColor", colorBeamTag);
        }
    }

}
