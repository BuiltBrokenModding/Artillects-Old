package artillects.hive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void loadTunnelTest()
    {
        //Center room
        peaces.add(new Structure(Building.TUNNELC, location.clone()));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(0, 0, 6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(0, 0, -6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(6, 0, 0)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(-6, 0, 0)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(6, 0, 6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(-6, 0, -6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(-6, 0, 6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(6, 0, -6)));
        peaces.add(new Structure(Building.TUNNELC, location.clone().add(6, 0, 6)));
        peaces.add(new Structure(Building.WALLX, location.clone().add(10, 1, 6)));
        peaces.add(new Structure(Building.WALLZ, location.clone().add(6, 1, 10)));
        peaces.add(new Structure(Building.WALLX, location.clone().add(-10, 1, 6)));
        peaces.add(new Structure(Building.WALLZ, location.clone().add(-6, 1, 10)));
        peaces.add(new Structure(Building.WALLX, location.clone().add(10, 1, -6)));
        peaces.add(new Structure(Building.WALLZ, location.clone().add(6, 1, -10)));
        peaces.add(new Structure(Building.WALLX, location.clone().add(-10, 1, -6)));
        peaces.add(new Structure(Building.WALLZ, location.clone().add(-6, 1, -10)));

        //NorthTunnel
        for (int i = 0; i < 3; i++)
            peaces.add(new Structure(Building.TUNNELZ, location.clone().add(0, 0, -12 - (6 * i))));

        //SouthTunnel
        for (int i = 0; i < 3; i++)
            peaces.add(new Structure(Building.TUNNELZ, location.clone().add(0, 0, 12 + (6 * i))));
        //EastTunnel
        //for (int i = 0; i < 3; i++)
            //peaces.add(new Structure(Building.TUNNELZ, location.clone().add(-12 - (6 * i), 0, 0)));

        //WestTunnel
        for (int i = 0; i < 3; i++)
            peaces.add(new Structure(Building.TUNNELZ, location.clone().add(12 + (6 * i), 0, 0)));
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
