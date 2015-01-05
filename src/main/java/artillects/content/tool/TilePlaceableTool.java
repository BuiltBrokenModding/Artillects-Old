package artillects.content.tool;

import artillects.core.Artillects;
import com.builtbroken.mc.api.tile.IRemovable;
import com.builtbroken.mc.api.tile.IRotatable;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.rotation.EulerAngle;
import com.builtbroken.mc.lib.transform.vector.IVectorWorld;
import com.builtbroken.mc.lib.transform.vector.Vector3;
import com.builtbroken.mc.prefab.tile.Tile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/** Prefab for tools that can be place and function while placed
 * 
 * @author Darkguardsman */
public class TilePlaceableTool extends Tile implements IPacketIDReceiver, IRemovable.ISneakPickup, IRotatable
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
    public void update()
    {
        super.update();
        if (doRayTrace && this.ticks % rayTiming == 0)
        {
            doRayTrace();
        }
    }

    @Override
    public boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (player.isSneaking())
            this.enabled = !this.enabled;
        else
            player.openGui(Artillects.INSTANCE, 0, world(), xi(), yi(), zi());
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
        angle.yaw_$eq(EulerAngle.clampAngleTo360(angle.yaw()));
        angle.pitch_$eq(EulerAngle.clampAngleTo360(angle.pitch()));

        if (this.loc == null)
            loc = new Vector3(x(), y(), z()).add(offset);

        MovingObjectPosition hit = getRayHit();
        if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            lastRayHit = new Vector3(hit.hitVec);
            sideHit = ForgeDirection.getOrientation(hit.sideHit);
        }
    }

    public MovingObjectPosition getRayHit()
    {
        Vector3 surveyor = new Vector3(x(), y(), z());
        Vector3 destination = angle.toVector();

        surveyor.add(destination);
        surveyor.add(offset);
        destination.multiply(rayDistance);
        destination.add(surveyor);

        return surveyor.rayTraceBlocks(world(), destination);
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
        ItemStack stack = new ItemStack(this.getBlockType(), 1, 0);
        drops.add(stack);
        return drops;
    }

    public void sendOnStatus()
    {
        if (world().isRemote)
            Engine.instance.packetHandler.sendToServer(new PacketTile(this, ENABLE_ID, this.enabled));
    }

    public void sendAngles()
    {
        sendPacket(new PacketTile(xi(), yi(), zi(), ROTATION_ID, this.angle.yaw(), this.angle.pitch()));
    }

    public void setRotation(double yaw, double pitch)
    {
        if (this.angle.yaw() != yaw || this.angle.pitch() != pitch)
        {
            this.angle.yaw_$eq(yaw);
            this.angle.pitch_$eq(pitch);
            if (world().isRemote)
            {
                PacketTile packet = new PacketTile(this, ROTATION_ID, yaw, pitch);
                sendPacket(packet);
            }
        }
    }

    @Override
    public PacketTile getDescPacket()
    {
        return new PacketTile(this, DESC_ID, this.angle.yaw(), this.angle.pitch(), this.enabled);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        angle.yaw_$eq(nbt.getDouble("yaw"));
        angle.pitch_$eq(nbt.getDouble("pitch"));
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
    public boolean read(ByteBuf data, int id,  EntityPlayer player, PacketType type)
    {
        if (id == ROTATION_ID)
        {
            angle.yaw_$eq(data.readDouble());
            angle.pitch_$eq(data.readDouble());
            return true;
        }
        else if (id == DESC_ID)
        {
            angle.yaw_$eq(data.readDouble());
            angle.pitch_$eq(data.readDouble());
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
        double yaw = angle.yaw();
        double pitch = angle.pitch();
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
                this.angle.yaw_$eq(0);
                break;
            case EAST:
                this.angle.yaw_$eq(90);
                break;
            case NORTH:
                this.angle.yaw_$eq(180);
                break;
            case WEST:
                this.angle.yaw_$eq(270);
                break;
            case DOWN:
                this.angle.pitch_$eq(-90);
                break;
            case UP:
                this.angle.pitch_$eq(90);
                break;
        }
    }

    /** Sends the desc packet to all players around this tile */
    public void sendDescPacket()
    {
        sendPacket(getDescPacket());
    }

    /** Sends the packet to all players around this tile
     * @param packet - packet to send */
    public void sendPacket(AbstractPacket packet)
    {
        sendPacket(packet, 64);
    }

    /** Sends the packet to all players around this tile
     * @param packet - packet to send
     * @param distance - distance in blocks to search for players
     */
    public void sendPacket(AbstractPacket packet, double distance)
    {
        Engine.instance.packetHandler.sendToAllAround(packet, ((IVectorWorld)this), distance);
    }
}
