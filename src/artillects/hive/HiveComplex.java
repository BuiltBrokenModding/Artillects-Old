package artillects.hive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;

import artillects.Vector3;
import artillects.VectorWorld;
import artillects.hive.structure.Building;
import artillects.hive.structure.Structure;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @author Dark */
public class HiveComplex extends HiveGhost
{
    protected VectorWorld location;
    protected String name;

    protected List<Structure> peaces = new ArrayList<Structure>();

    public HiveComplex(String name, VectorWorld location)
    {
        this.name = name;
        this.location = location;
        Hive.instance().addHiveComplex(this);
    }

    public void loadGeneralBuilding()
    {
        final int width = 3;
        final int height = 4;
        for (int floor = 0; floor <= height; floor++)
        {
            VectorWorld floorBase = this.location.clone().add(0, 8 * floor, 0);
            //Center room
            peaces.add(new Structure(Building.NODE, floorBase.clone()));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, 6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, -6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(6, 0, 0)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(-6, 0, 0)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(6, 0, 6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(-6, 0, -6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(-6, 0, 6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(6, 0, -6)));
            peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(6, 0, 6)));
            peaces.add(new Structure(Building.WALLX, floorBase.clone().add(10, 1, 6)));
            peaces.add(new Structure(Building.WALLZ, floorBase.clone().add(6, 1, 10)));
            peaces.add(new Structure(Building.WALLX, floorBase.clone().add(-10, 1, 6)));
            peaces.add(new Structure(Building.WALLZ, floorBase.clone().add(-6, 1, 10)));
            peaces.add(new Structure(Building.WALLX, floorBase.clone().add(10, 1, -6)));
            peaces.add(new Structure(Building.WALLZ, floorBase.clone().add(6, 1, -10)));
            peaces.add(new Structure(Building.WALLX, floorBase.clone().add(-10, 1, -6)));
            peaces.add(new Structure(Building.WALLZ, floorBase.clone().add(-6, 1, -10)));

            if (floor != height)
            {
                //Tunnels out from room
                for (int i = 0; i < width; i++)
                {
                    peaces.add(new Structure(Building.TUNNELZ, floorBase.clone().add(0, 0, -12 - (6 * i))));
                    peaces.add(new Structure(Building.TUNNELZ, floorBase.clone().add(0, 0, 12 + (6 * i))));
                    peaces.add(new Structure(Building.TUNNELX, floorBase.clone().add(-12 - (6 * i), 0, 0)));
                    peaces.add(new Structure(Building.TUNNELX, floorBase.clone().add(12 + (6 * i), 0, 0)));
                }

                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, -12 - (6 * width))));
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(0, 0, +12 + (6 * width))));
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(-12 - (6 * width), 0, 0)));
                peaces.add(new Structure(Building.TUNNELC, floorBase.clone().add(+12 + (6 * width), 0, 0)));
            }
        }
    }

    public void loadTunnel(VectorWorld start, ForgeDirection direction, int tunnelPeaces)
    {
        for (int i = 0; i < tunnelPeaces; i++)
        {
            peaces.add(new Structure(Building.TUNNELZ, start.clone().add(direction.offsetX * 6, direction.offsetX * 6, direction.offsetX * 6)));
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
}
