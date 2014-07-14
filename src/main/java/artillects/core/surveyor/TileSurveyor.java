package artillects.core.surveyor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IRemovable.ISneakPickup;
import resonant.lib.content.module.TileBase;
import resonant.lib.network.IPacketReceiverWithID;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.vector.Cuboid;
import universalelectricity.api.vector.EulerAngle;
import universalelectricity.api.vector.Vector3;
import artillects.core.Artillects;

import com.google.common.io.ByteArrayDataInput;

/** Small camera looking block that can deploy laser lines, gauge distances, and do other utilities.
 * 
 * @author Darkguardsman */
public class TileSurveyor extends TileBase implements IPacketReceiverWithID, ISneakPickup
{
    protected EulerAngle angle;
    protected Color beamColor = Color.orange;
    protected Vector3 lastRayHit = null;
    protected Vector3 loc = null;
    protected Vector3 offset = new Vector3(0.5, 0.5, 0.5);

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
                loc = new Vector3(this).translate(offset);

            MovingObjectPosition hit = getRayHit();
            if (hit != null && hit.typeOfHit == EnumMovingObjectType.TILE)
            {
                lastRayHit = new Vector3(hit).translate(offset);
            }
            //TODO render distance above tile
            if (world().isRemote)
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
        surveyor.translate(offset);
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

    @Override
    public boolean onReceivePacket(int id, ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        if (world().isRemote)
        {
            if (id == 0)
            {
                this.angle.yaw = data.readDouble();
                this.angle.pitch = data.readDouble();
                this.beamColor = new Color(data.readInt(), data.readInt(), data.readInt());
                return true;
            }
            else if (id == 1)
            {
                this.angle.yaw = data.readDouble();
                this.angle.pitch = data.readDouble();
                return true;
            }
        }
        else
        {
            if (id == 0)
            {
                this.angle.yaw = data.readDouble();
                return true;
            }
            else if (id == 1)
            {
                this.angle.pitch = data.readDouble();
                return true;
            }
        }
        return false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return Artillects.PACKET_TILE.getPacketWithID(1, this, this.angle.yaw, this.angle.pitch, this.beamColor.getRed(), this.beamColor.getGreen(), this.beamColor.getBlue());
    }

    public void sendAngles()
    {
        Packet packet = Artillects.PACKET_TILE.getPacketWithID(1, this, this.angle.yaw, this.angle.pitch);
        PacketHandler.sendPacketToClients(packet, world(), new Vector3(this), 100);
    }

    public void setYaw(double yaw)
    {
        if (this.angle.yaw != yaw)
        {
            this.angle.yaw = yaw;
            if (world().isRemote)
            {
                Packet packet = Artillects.PACKET_TILE.getPacketWithID(0, this, yaw);
                PacketHandler.sendPacketToClients(packet, world(), new Vector3(this), 100);
            }
        }
    }

    public void setPitch(double pitch)
    {
        if (this.angle.pitch != pitch)
        {
            this.angle.pitch = pitch;
            if (world().isRemote)
            {
                Packet packet = Artillects.PACKET_TILE.getPacketWithID(1, this, pitch);
                PacketHandler.sendPacketToClients(packet, world(), new Vector3(this), 100);
            }
        }
    }

    @Override
    public List<ItemStack> getRemovedItems(EntityPlayer entity)
    {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        ItemStack stack = new ItemStack(this.blockID(), 1, 0);
        stacks.add(stack);
        return stacks;
    }
}
