package artillects.hive;

import net.minecraft.nbt.NBTTagCompound;

/** Applied to object that need to be saved/loaded but don't contain save/load by default
 * 
 * @author DarkGuardsman */
public interface ISaveObject
{
    public void save(NBTTagCompound nbt);

    public void load(NBTTagCompound nbt);
}
