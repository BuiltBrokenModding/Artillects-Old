package artillects.hive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import artillects.Vector3;
import artillects.hive.structure.Structure;

/** Hive village in other words. This represents a single location in the hive. Each hive complex has
 * a task and set of structure peaces.
 * 
 * @author Dark */
public class HiveComplex
{
    protected Vector3 location;
    protected String name;

    protected List<Structure> peaces = new ArrayList<Structure>();

    public HiveComplex(String name, Vector3 location)
    {
        this.name = name;
        this.location = location;
    }

    public void update()
    {
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

    public boolean isValid()
    {
        return location != null && peaces != null && !peaces.isEmpty();
    }

    public void invalidate()
    {
        peaces.clear();
    }
}
