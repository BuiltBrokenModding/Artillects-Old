package artillects.core.tool.surveyor;

import java.awt.Color;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import resonant.api.IRemovable.ISneakPickup;
import resonant.api.ITagRender;
import resonant.lib.network.IPacketReceiverWithID;
import resonant.lib.network.PacketHandler;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import artillects.core.Artillects;
import artillects.core.tool.TilePlaceableTool;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

/** Small camera looking block that can deploy laser lines, gauge distances, and do other utilities.
 * 
 * @author Darkguardsman */
public class TileSurveyor extends TilePlaceableTool implements IPacketReceiverWithID, ISneakPickup, ITagRender
{
    protected Color beamColor = Color.cyan;

    public TileSurveyor()
    {
        super(UniversalElectricity.machine);
        rayDistance = 1000;
        doRayTrace = true;
        isOpaqueCube = false;
        normalRender = false;
        customItemRender = true;
        itemBlock = ItemSurveyor.class;
        textureName = "material_steel";
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.ticks % 3 == 0)
        {
            //TODO render distance above tile
            if (lastRayHit != null && world().isRemote && enabled)
                Artillects.proxy.renderLaser(world(), loc, lastRayHit, beamColor, 3);
        }
    }
    
    @Override
    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (!player.isSneaking())
        {
            player.openGui(Artillects.INSTANCE, 0, world(), x(), y(), z());
            return true;
        }
        return super.use(player, side, hit);
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
        if (nbt.hasKey("beamColor"))
        {
            NBTTagCompound tag = nbt.getCompoundTag("beamColor");
            this.beamColor = new Color(tag.getInteger("red"), tag.getInteger("green"), tag.getInteger("blue"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagCompound colorBeamTag = new NBTTagCompound();
        colorBeamTag.setInteger("red", beamColor.getRed());
        colorBeamTag.setInteger("blue", beamColor.getBlue());
        colorBeamTag.setInteger("green", beamColor.getGreen());
        nbt.setCompoundTag("beamColor", colorBeamTag);
    }

    @Override
    public boolean onReceivePacket(int id, ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        if (id == DESC_ID)
        {
            this.angle.yaw = data.readDouble();
            this.angle.pitch = data.readDouble();
            this.enabled = data.readBoolean();
            this.beamColor = new Color(data.readInt(), data.readInt(), data.readInt());
            return true;
        }
        else if (super.onReceivePacket(id, data, player, extra))
        {
            return true;
        }
        else if (id == 3)
        {
            this.beamColor = new Color(data.readInt(), data.readInt(), data.readInt());
            return true;
        }
        return false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return Artillects.PACKET_TILE.getPacketWithID(DESC_ID, this, this.angle.yaw, this.angle.pitch, this.enabled, this.beamColor.getRed(), this.beamColor.getGreen(), this.beamColor.getBlue());
    }

    public void setColor(Color color)
    {
        this.beamColor = color;
        Packet packet = Artillects.PACKET_TILE.getPacketWithID(3, this, this.beamColor.getRed(), this.beamColor.getGreen(), this.beamColor.getBlue());
        if (world().isRemote)
            PacketDispatcher.sendPacketToServer(packet);
    }

    @Override
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player)
    {
        if (enabled)
        {
            double distance = distance();
            int i = (int) (distance * 100);
            distance = i / 100;
            map.put("" + distance, 0xFFFFFF);
            map.put("" + getDirection().name(), 0xFFFFFF);
        }
        return 1.5f;
    }
}
