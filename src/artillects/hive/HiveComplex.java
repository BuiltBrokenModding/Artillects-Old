package artillects.hive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import artillects.VectorWorld;
import artillects.hive.structure.Building;
import artillects.hive.structure.Structure;
import artillects.hive.zone.ZoneBuilding;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @author Dark */
public class HiveComplex extends HiveGhost
{
    protected VectorWorld location;
    protected String name;

    protected final List<Structure> peaces = new ArrayList<Structure>();
    public final List<Structure> damagedPeaces = new ArrayList<Structure>();

    protected ZoneBuilding buildZone;

    public HiveComplex(String name, VectorWorld location)
    {
        this.name = name;
        this.location = location;
        Hive.instance().addHiveComplex(this);
    }

    public void loadGeneralBuilding(boolean worldGen)
    {
        final int width = 3;
        final int height = 4;
        final int tunnelSpacing = 12;
        for (int floor = 0; floor <= height; floor++)
        {
            VectorWorld floorBase = this.location.clone().add(0, 8 * floor, 0);
            //Center room
            peaces.add(new Structure(Building.NODE, floorBase.clone(), worldGen));
            if (floor == 0)
            {
                peaces.add(new Structure(Building.FLOOR, floorBase.clone(), worldGen));
            }
            this.load3x3Room(floorBase, worldGen);

            if (floor != height)
            {
                //NorthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, -tunnelSpacing), ForgeDirection.NORTH, width, worldGen);
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, -tunnelSpacing - (6 * width)), worldGen));
                this.loadTunnel(floorBase.clone().add(-6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.WEST, width - 1, worldGen);
                this.loadTunnel(floorBase.clone().add(+6, 0, -tunnelSpacing - (6 * width)), ForgeDirection.EAST, width - 1, worldGen);
                //SouthTunnel
                this.loadTunnel(floorBase.clone().add(0, 0, +tunnelSpacing), ForgeDirection.SOUTH, width, worldGen);
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, +tunnelSpacing + (6 * width)), worldGen));
                this.loadTunnel(floorBase.clone().add(-6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.WEST, width - 1, worldGen);
                this.loadTunnel(floorBase.clone().add(+6, 0, +tunnelSpacing + (6 * width)), ForgeDirection.EAST, width - 1, worldGen);
                //EastTunnel
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing, 0, 0), ForgeDirection.EAST, width, worldGen);
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 0), worldGen));
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, -6), ForgeDirection.NORTH, width - 1, worldGen);
                this.loadTunnel(floorBase.clone().add(+tunnelSpacing + (6 * width), 0, 6), ForgeDirection.SOUTH, width - 1, worldGen);
                //WestTunnel
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing, 0, 0), ForgeDirection.WEST, width, worldGen);
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 0), worldGen));
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, -6), ForgeDirection.NORTH, width - 1, worldGen);
                this.loadTunnel(floorBase.clone().add(-tunnelSpacing - (6 * width), 0, 6), ForgeDirection.SOUTH, width - 1, worldGen);
            }
        }

        this.buildZone = new ZoneBuilding(this.location.clone().add(0, 25, 0), this, 50);

    }

    public void load3x3Room(VectorWorld start, boolean worldGen)
    {
        //First Peace
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, 0), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, -6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, 0), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, 0), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, -6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, -6), worldGen));

        peaces.add(new Structure(Building.WALLX, start.clone().add(10, 1, 6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(6, 1, 10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(-10, 1, 6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(-6, 1, 10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(10, 1, -6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(6, 1, -10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(-10, 1, -6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(-6, 1, -10), worldGen));

    }

    public void load5x5Room(VectorWorld start, int expand, boolean worldGen)
    {
        //Center
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, 0), worldGen));
        //Sout
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, 12), worldGen));
        //North
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, -6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(0, 0, -12), worldGen));
        //East
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, 0), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(12, 0, 0), worldGen));
        //West
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, 0), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-12, 0, 0), worldGen));
        //Corners
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, -6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(-6, 0, 6), worldGen));
        peaces.add(new Structure(Building.TUNNELC, start.clone().add(6, 0, -6), worldGen));
        //Walls
        peaces.add(new Structure(Building.WALLX, start.clone().add(10, 1, 6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(6, 1, 10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(-10, 1, 6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(-6, 1, 10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(10, 1, -6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(6, 1, -10), worldGen));
        peaces.add(new Structure(Building.WALLX, start.clone().add(-10, 1, -6), worldGen));
        peaces.add(new Structure(Building.WALLZ, start.clone().add(-6, 1, -10), worldGen));

    }

    public void loadTunnel(VectorWorld start, ForgeDirection direction, int tunnelPeaces, boolean worldGen)
    {
        for (int i = 0; i < tunnelPeaces; i++)
        {
            VectorWorld spot = start.clone().add(direction.offsetX * (6 * i), direction.offsetY * (6 * i), direction.offsetZ * (6 * i));
            if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH)
            {
                peaces.add(new Structure(Building.TUNNELZ, spot, worldGen));
            }
            else if (direction == ForgeDirection.EAST || direction == ForgeDirection.WEST)
            {
                peaces.add(new Structure(Building.TUNNELX, spot, worldGen));
            }
            else if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
            {
                peaces.add(new Structure(Building.NODE, spot, worldGen));
                peaces.add(new Structure(Building.WALLX, spot.clone().add(3, 1, 0), worldGen));
                peaces.add(new Structure(Building.WALLZ, spot.clone().add(-3, 1, 0), worldGen));
                peaces.add(new Structure(Building.WALLX, spot.clone().add(0, 1, -3), worldGen));
                peaces.add(new Structure(Building.WALLZ, spot.clone().add(0, 1, 3), worldGen));
            }
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        Iterator<Structure> it = peaces.iterator();
        while (it.hasNext())
        {
            Structure str = it.next();
            try
            {
                if (str.isValid())
                {
                    str.updateEntity();
                    if (str.isDamaged() && !this.damagedPeaces.contains(str))
                    {
                        this.damagedPeaces.add(str);
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
    }

    @Override
    public boolean isValid()
    {
        return location != null && peaces != null && !peaces.isEmpty();
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
        nbt.setCompoundTag("location", this.location.save(new NBTTagCompound()));

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.location = new VectorWorld(nbt.getCompoundTag("location"));

    }
}
