package artillects.drone;

import artillects.core.building.BuildingPart;
import artillects.core.building.EnumStructurePeaces;
import artillects.core.building.GhostObject;
import artillects.core.zone.Zone;
import artillects.core.zone.ZoneBuilding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent.Unload;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import resonant.lib.utility.nbt.SaveManager;
import universalelectricity.core.transform.vector.VectorWorld;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @TODO setup building priority so that the entire hive doesn't world gen or construct at one time
 * 
 * @author Dark */
public class HiveComplex extends GhostObject implements IVirtualObject
{
    public VectorWorld location;
    private String name;

    protected final List<BuildingPart> peaces = new ArrayList<BuildingPart>();
    public final List<BuildingPart> damagedPeaces = new ArrayList<BuildingPart>();

    protected ZoneBuilding buildZone;

    public HashSet<Zone> zones = new HashSet<Zone>();

    public boolean playerZone = false;

    private static HiveComplex playerHiveComplex;

    public static HiveComplex getPlayerHive()
    {
        if (playerHiveComplex == null)
        {
            playerHiveComplex = new HiveComplex(true);
        }
        return playerHiveComplex;
    }

    public HiveComplex()
    {
        SaveManager.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public HiveComplex(String name, VectorWorld location)
    {
        this();
        this.setName(name);
        this.location = location;
        //HiveComplexManager.instance().register(this);
    }

    public HiveComplex(boolean player)
    {
        this("PlayerZone", new VectorWorld(WorldProvider.getProviderForDimension(0).worldObj, 0, 255, 0));
        this.playerZone = true;
    }

    public void addZone(Zone zone)
    {
        this.zones.add(zone);
    }

    public void loadFabricatorDemo()
    {
        buildZone = new ZoneBuilding(this, 50);
        this.load3x3Room(this.location.clone(), 2);
        for (int i = 0; i < 1; i++)
        {
            //EntityFabricator fab = new EntityFabricator(this.location.world);
            //fab.setPosition(this.location.x + 0.5, this.location.y + 1 + (i * 0.5), this.location.z + 0.5);
            //fab.setOwner(HiveComplexManager.instance());
            //buildZone.assignArtillect(fab);
            //this.location.world.spawnEntityInWorld(fab);
        }
    }

    public void loadGeneralBuilding(boolean worldGen)
    {
        final int width = 4;
        final int height = 3;
        final int tunnelSpacing = 18;
        for (int floor = 0; floor <= height; floor++)
        {
            VectorWorld floorBase = this.location.clone().add(0, 8 * floor, 0);
            // Center room
            peaces.add(new BuildingPart(EnumStructurePeaces.NODE, floorBase.clone()));
            if (floor == 0)
            {
                peaces.add(new BuildingPart(EnumStructurePeaces.FLOOR, floorBase.clone()));
            }
            if (floor == 0)
            {
                this.load5x5Room(floorBase, 2);
            }
            if (floor == 7 || floor == 8)
            {
                this.load5x5Room(floorBase, 1);
            }
            else
            {
                this.load5x5Room(floorBase, 0);
            }
            if (floor != height)
            {
                // NorthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, -tunnelSpacing), ForgeDirection.NORTH, width);
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, floorBase.clone().add(0, 0, -tunnelSpacing - (6 * width))));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, floorBase.clone().add(0, 1, -tunnelSpacing - (6 * width) - 4)));
                this.loadTunnel(floorBase.clone().add(-6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.WEST, width + 1);
                this.loadTunnel(floorBase.clone().add(+6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.EAST, width + 1);
                // SouthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, +tunnelSpacing), ForgeDirection.SOUTH, width);
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, floorBase.clone().add(0, 0, +tunnelSpacing + (6 * width))));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, floorBase.clone().add(0, 1, +tunnelSpacing + (6 * width) + 4)));
                this.loadTunnel(floorBase.clone().add(-6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.WEST, width + 1);
                this.loadTunnel(floorBase.clone().add(+6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.EAST, width + 1);
                // EastTunnel
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing, 0, 0), ForgeDirection.EAST, width);
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 0)));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, floorBase.clone().add(+tunnelSpacing + (6 * width) + 4, 1, 0)));
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, -6), ForgeDirection.NORTH, width + 1);
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 6), ForgeDirection.SOUTH, width + 1);
                // WestTunnel
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing, 0, 0), ForgeDirection.WEST, width);
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 0)));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, floorBase.clone().add(-tunnelSpacing - (6 * width) - 4, 1, 0)));
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, -6), ForgeDirection.NORTH, width + 1);
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 6), ForgeDirection.SOUTH, width + 1);

                // Corner room
                int cornerA = 0, cornerB = 0, cornerC = 0, cornerD = 0;
                switch (floor)
                {
                    case 0:
                        cornerA = 1;
                        break;
                    case 1:
                        cornerA = 1;
                        cornerB = 1;
                        break;
                    case 2:
                        cornerB = 1;
                        cornerC = 1;
                        break;
                    case 3:
                        cornerC = 1;
                        cornerD = 1;
                        break;
                    case 4:
                        cornerD = 1;
                        cornerA = 1;
                        break;
                    case 5:
                        cornerA = 1;
                        cornerB = 1;
                        break;
                    case 6:
                        cornerB = 1;
                        cornerC = 1;
                        break;
                }
                // NorthWest
                this.load3x3Room(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, -(6 * (width + 1)) - 12), cornerA);
                // NorthEast
                this.load3x3Room(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, +(6 * (width + 1)) + 12), cornerB);
                // SouthEast
                this.load3x3Room(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, +(6 * (width + 1)) + 12), cornerC);
                // SouthWest
                this.load3x3Room(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, -(6 * (width + 1)) - 12), cornerD);
            }
        }

        //this.buildZone = new ZoneBuilding(this, 80);

        if (worldGen)
        {
            for (BuildingPart str : this.peaces)
            {
                str.worldGen();
            }
        }

        for (int i = 0; i < 4; i++)
        {
            //EntityFabricator fab = new EntityFabricator(this.location.world);
            //fab.setPosition(this.location.x + 0.5, this.location.y + (i * 0.5), this.location.z + 0.5);
            //fab.setOwner(HiveComplexManager.instance());
            //this.buildZone.assignArtillect(fab);
            //this.location.world.spawnEntityInWorld(fab);
        }
    }

    public void load3x3Room(VectorWorld start, int center)
    {
        // Center
        if (center == 0)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, 0)));
        }
        else if (center == 1)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.TELEFLOOR, start.clone().add(0, 0, 0)));
        }
        else if (center == 2)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.COREFLOOR, start.clone().add(0, 0, 0)).worldGen());
        }
        // South
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, 6)));
        // North
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, -6)));
        // East
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, 0)));
        // West
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, 0)));
        // Corners
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, -6)));
        // Walls
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(10, 1, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(6, 1, 10)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-10, 1, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-6, 1, 10)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(10, 1, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(6, 1, -10)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-10, 1, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-6, 1, -10)));

    }

    public void load5x5Room(VectorWorld start, int center)
    {
        // Center
        if (center == 0)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, 0)));
        }
        else if (center == 1)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.TELEFLOOR, start.clone().add(0, 0, 0)));
        }
        else if (center == 2)
        {
            peaces.add(new BuildingPart(EnumStructurePeaces.SKYLIGHT, start.clone().add(0, +7, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.NODE, start.clone().add(0, 0, 0)));
            peaces.add(new BuildingPart(EnumStructurePeaces.COREFLOOR, start.clone().add(0, 0, 0)).worldGen());
        }
        // South
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(+6, 0, 12)));
        // North
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(0, 0, -12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, -12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(+6, 0, -12)));
        // East
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, 0)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(12, 0, 0)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(12, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(12, 0, 6)));
        // West
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, 0)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-12, 0, 0)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-12, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-12, 0, 6)));
        // Corners
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-6, 0, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(6, 0, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(12, 0, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-12, 0, -12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(-12, 0, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELC, start.clone().add(12, 0, -12)));
        // Walls
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(16, 1, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(6, 1, 16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-16, 1, 6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-6, 1, 16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(16, 1, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(6, 1, -16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-16, 1, -6)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-6, 1, -16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(16, 1, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(12, 1, 16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-16, 1, 12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-12, 1, 16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(16, 1, -12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(12, 1, -16)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, start.clone().add(-16, 1, -12)));
        peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, start.clone().add(-12, 1, -16)));

    }

    public void loadTunnel(VectorWorld start, ForgeDirection direction, int tunnelPeaces)
    {
        for (int i = 0; i < tunnelPeaces; i++)
        {
            VectorWorld spot = start.clone().add(direction.offsetX * (6 * i), direction.offsetY * (6 * i), direction.offsetZ * (6 * i));
            if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH)
            {
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELZ, spot));
            }
            else if (direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
            {
                peaces.add(new BuildingPart(EnumStructurePeaces.TUNNELX, spot));
            }
            else if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
            {
                peaces.add(new BuildingPart(EnumStructurePeaces.NODE, spot));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, spot.clone().add(3, 1, 0)));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, spot.clone().add(-3, 1, 0)));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLX, spot.clone().add(0, 1, -3)));
                peaces.add(new BuildingPart(EnumStructurePeaces.WALLZ, spot.clone().add(0, 1, 3)));
            }
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.ticks % 20 == 0)
        {
            Iterator<BuildingPart> it = peaces.iterator();
            while (it.hasNext())
            {
                BuildingPart str = it.next();
                try
                {
                    if (str.isValid())
                    {
                        // str.updateEntity();
                        if (str.isDamaged() && !this.damagedPeaces.contains(str))
                        {
                            // this.damagedPeaces.add(str);
                        }
                    }
                    else
                    {
                        str.invalidate();
                        it.remove();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            if (this.zones != null && !this.zones.isEmpty())
            {
                Iterator<Zone> itZone = zones.iterator();
                while (itZone.hasNext())
                {
                    Zone zone = itZone.next();
                    if (zone != null)
                    {
                        if (zone.isValid())
                        {
                            zone.updateEntity();
                        }
                        else
                        {
                            zone.invalidate();
                            itZone.remove();
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean isValid()
    {
        return super.isValid() && location != null && peaces != null && !peaces.isEmpty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        peaces.clear();
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        nbt.setTag("location", this.location.writeNBT(new NBTTagCompound()));
        nbt.setString("name", this.getName());
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new VectorWorld(nbt.getCompoundTag("location"));
        this.loadGeneralBuilding(false);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public File getSaveFile()
    {
        return new File(NBTUtility.getSaveDirectory(), "hive/" + this.location.world().provider.dimensionId + "/complex_" + this.name);
    }

    @Override
    public void setSaveFile(File file)
    {
        // TODO Auto-generated method stub

    }

    @SubscribeEvent
    public void onWorldunLoad(Unload event)
    {
        if (event.world == this.location.world())
        {
            this.invalidate();
            //HiveComplexManager.instance().unloadHive(this);
        }
    }
}
