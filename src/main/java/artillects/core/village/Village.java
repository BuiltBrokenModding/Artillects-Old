package artillects.core.village;

import universalelectricity.api.vector.VectorWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import artillects.core.building.GhostObject;

public class Village extends GhostObject
{
    protected int radius = 10;

    private Village()
    {
        
    }
    
    public Village(VectorWorld vec , int radius)
    {
        this(vec.world, vec.intX(), vec.intY(), vec.intZ(), radius);
    }
    
    public Village(World world, int x, int y, int z, int radius)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }
    
    public static void loadVillageFromSave(NBTTagCompound tag)
    {
        Village village = new Village();
        village.load(tag);
    }
}
