package artillects.core.region;

import java.util.HashMap;

import artillects.core.interfaces.IID;
import net.minecraft.world.World;

/** Section of land that contains dozens of sub land areas. All sub land must be contained with in
 * the controllers area.
 * 
 * @author Darkguardsman */
public class LandController extends Land implements IID<Integer, LandController>
{
    private int id = 0;
    private int nextLandID = 0;
    private HashMap<Integer, Land> land = new HashMap<Integer, Land>();

    public LandController()
    {
        super();
    }

    public LandController(World world, int x, int y, int z, int size)
    {
        super(world, x, y, z, size);
    }

    public void addLand(Land land)
    {
        if (!this.land.containsValue(land))
        {
            this.land.put(nextLandID++, land);
        }
    }

    @Override
    public Integer getID()
    {
        return this.id;
    }

    @Override
    public LandController setID(Integer id)
    {
        this.id = id;
        return this;
    }
}
