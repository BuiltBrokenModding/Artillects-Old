package artillects.drone.hive;

import java.io.File;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import calclavia.lib.utility.nbt.IVirtualObject;
import calclavia.lib.utility.nbt.SaveManager;

/** AI that controls the majority of macro functions for the hive
 * 
 * @author Darkguardsman */
public class Hivemind implements IVirtualObject
{
    private String hiveName = "world";
    private final HashMap<Integer, HiveWorld> hiveWorlds = new HashMap<Integer, HiveWorld>();

    public Hivemind()
    {
        SaveManager.register(this);
    }

    public Hivemind(String name)
    {
        this();
        this.hiveName = name;
    }

    public void update()
    {

    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public File getSaveFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSaveFile(File file)
    {
        // TODO Auto-generated method stub
        
    }
}
