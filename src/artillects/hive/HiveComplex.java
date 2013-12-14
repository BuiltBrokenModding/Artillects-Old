package artillects.hive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import artillects.Vector3;
import artillects.hive.structure.Building;
import artillects.hive.structure.Structure;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @author Dark */
public class HiveComplex extends HiveGhost
{
    protected Vector3 location;
    protected String name;    

    protected List<Structure> peaces = new ArrayList<Structure>();

    public HiveComplex(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
        Hive.instance().addHiveComplex(this);
    }

    public void loadTunnelTest()
    {
        peaces.add(new Structure(Building.TUNNELC, location));
        peaces.add(new Structure(Building.TUNNELC, location.add(0, 0, 5)));
    }

    @Override
    public void updateEntity()
    {
        System.out.println("HiveComplex[" + location.toString() + "] Update");
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
