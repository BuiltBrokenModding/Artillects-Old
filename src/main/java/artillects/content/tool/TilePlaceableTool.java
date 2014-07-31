package artillects.content.tool;

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
import resonant.lib.network.IPacketReceiverWithID;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.tile.TileElectrical;
import universalelectricity.api.vector.EulerAngle;
import universalelectricity.api.vector.Vector3;
import artillects.core.Artillects;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

/** Prefab for tools that can be place and function while placed
 * 
 * @author Darkguardsman */
public class TilePlaceableTool extends TileElectrical implements IPacketReceiverWithID, ISneakPickup
{
    public EulerAngle angle;

    protected ForgeDirection sideHit;
    protected Vector3 lastRayHit;
    protected Vector3 loc;
    protected Vector3 offset;

    protected double rayDistance = 10;
    protected boolean doRayTrace = false;
    protected int rayTiming = 3;

    public boolean enabled = true;

    public static final int ROTATION_ID = 0;
    public static final int ENABLE_ID = 1;
    public static final int DESC_ID = 2;

    public TilePlaceableTool(Material material)
    {
        super(material);
        offset = new Vector3(0.5, 0.5, 0.5);
        angle = new EulerAngle(ForgeDirection.NORTH);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (doRayTrace && this.ticks % rayTiming == 0)
        {
            doRayTrace();
        }
    }

    @Override
    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (player.isSneaking())
            this.enabled = !this.enabled;
        else
            player.openGui(Artillects.INSTANCE, 0, world(), x(), y(), z());
        return true;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    public void doRayTrace()
    {
        lastRayHit = null;
        sideHit = null;
        angle.yaw = EulerAngle.clampAngleTo360(angle.yaw);
        angle.pitch = EulerAngle.clampAngleTo360(angle.pitch);

        if (this.loc == null)
            loc = new Vector3(this).translate(offset);

        MovingObjectPosition hit = getRayHit();
        if (hit != null && hit.typeOfHit == EnumMovingObjectType.TILE)
        {
            lastRayHit = new Vector3(hit.hitVec);
            sideHit = ForgeDirection.getOrientation(hit.sideHit);
        }
    }

    public MovingObjectPosition getRayHit()
    {
        Vector3 surveyor = new Vector3(this);
        Vector3 destination = new Vector3(angle);

        surveyor.add(destination);
        surveyor.translate(offset);
        destination.scale(rayDistance);
        destination.add(surveyor);

        return surveyor.rayTraceBlocks(world(), destination, true);
    }

    @Override
    public List<ItemStack> getRemovedItems(EntityPlayer entity)
    {
        return getDrops(0, 0);
    }

    @Override
    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack stack = new ItemStack(this.blockID(), 1, 0);
        drops.add(stack);
        return drops;
    }

    public void sendOnStatus()
    {
        if (world().isRemote)
            PacketDispatcher.sendPacketToServer(Artillects.PACKET_TILE.getPacketWithID(ENABLE_ID, this, this.enabled));
    }

    public void sendAngles()
    {
        PacketHandler.sendPacketToClients(Artillects.PACKET_TILE.getPacketWithID(ROTATION_ID, this, this.angle.yaw, this.angle.pitch), world(), new Vector3(this), 100);
    }

    public void setRotation(double yaw, double pitch)
    {
        if (this.angle.yaw != yaw || this.angle.pitch != pitch)
        {
            this.angle.yaw = yaw;
            this.angle.pitch = pitch;
            if (world().isRemote)
            {
                Packet packet = Artillects.PACKET_TILE.getPacketWithID(ROTATION_ID, this, yaw, pitch);
                PacketDispatcher.sendPacketToServer(packet);
            }
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return Artillects.PACKET_TILE.getPacketWithID(DESC_ID, this, this.angle.yaw, this.angle.pitch, this.enabled);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        angle.yaw = nbt.getDouble("yaw");
        angle.pitch = nbt.getDouble("pitch");
        if (nbt.hasKey("enabled"))
            this.enabled = nbt.getBoolean("enabled");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setDouble("yaw", angle.yaw());
        nbt.setDouble("pitch", angle.pitch());
        nbt.setBoolean("enabled", this.enabled);
    }

    @Override
    public boolean onReceivePacket(int id, ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        if (id == ROTATION_ID)
        {
            angle.yaw = data.readDouble();
            angle.pitch = data.readDouble();
            return true;
        }
        else if (id == DESC_ID)
        {
            angle.yaw = data.readDouble();
            angle.pitch = data.readDouble();
            enabled = data.readBoolean();
            return true;
        }
        else if (id == ENABLE_ID)
        {
            enabled = data.readBoolean();
            return true;
        }
        return false;
    }

    @Override
    public ForgeDirection getDirection()
    {
        double yaw = angle.yaw;
        double pitch = angle.pitch;
        if (pitch == 90)
            return ForgeDirection.UP;
        if (pitch == -90)
            return ForgeDirection.DOWN;

        if (45 >= yaw && yaw >= -45)
            return ForgeDirection.NORTH;

        if (127 >= yaw && yaw >= 45)
            return ForgeDirection.WEST;

        if (-45 >= yaw && yaw >= -127)
            return ForgeDirection.EAST;

        if (127 >= yaw || yaw >= -127)
            return ForgeDirection.SOUTH;

        return ForgeDirection.UNKNOWN;
    }

    @Override
    public void setDirection(ForgeDirection direction)
    {
        switch (direction)
        {
            case SOUTH:
                this.angle.yaw = 0;
                break;
            case EAST:
                this.angle.yaw = 90;
                break;
            case NORTH:
                this.angle.yaw = 180;
                break;
            case WEST:
                this.angle.yaw = 270;
                break;
            case DOWN:
                this.angle.pitch = -90;
                break;
            case UP:
                this.angle.pitch = 90;
                break;
        }
    }

}
