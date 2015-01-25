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
import com.builtbroken.mc.lib.transform.vector.Pos;
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
public abstract class TilePlaceableTool extends Tile implements IPacketIDReceiver, IRemovable.ISneakPickup, IRotatable
{
    public EulerAngle angle;

    protected ForgeDirection sideHit;
    protected Pos lastRayHit;
    protected Pos loc;
    protected Pos offset;

    protected double rayDistance = 10;
    protected boolean doRayTrace = false;
    protected int rayTiming = 3;

    public boolean enabled = true;

    public static final int ROTATION_ID = 0;
    public static final int ENABLE_ID = 1;
    public static final int DESC_ID = 2;

    public TilePlaceableTool(String name, Material material)
    {
        super(name, material);
        offset = new Pos(0.5, 0.5, 0.5);
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
    public boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
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
            loc = new Pos(x(), y(), z()).add(offset);

        MovingObjectPosition hit = getRayHit();
        if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            lastRayHit = new Pos(hit.hitVec);
            sideHit = ForgeDirection.getOrientation(hit.sideHit);
        }
    }

    public MovingObjectPosition getRayHit()
    {
        Pos surveyor = new Pos(x(), y(), z());
        Pos destination = angle.toVector();

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
        sendPacket(new PacketTile(this, ROTATION_ID, this.angle.yaw(), this.angle.pitch()));
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
    public boolean read(ByteBuf data, int id, EntityPlayer player, PacketType type)
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
}
