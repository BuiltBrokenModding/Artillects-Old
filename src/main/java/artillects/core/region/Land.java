package artillects.core.region;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import artillects.core.interfaces.IID;
import resonant.lib.utility.nbt.IVirtualObject;
import resonant.lib.utility.nbt.NBTUtility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** Land based location
 * 
 * @author Darkguardsman */
public class Land extends FactionObject implements IID
{
    private int x_start = 0, y_start = 0, z_start = 0;
    private int x_end = 0, y_end = 0, z_end = 0;

    /** Name of the region of land */
    private String name = "Land";
    private UUID id = null;

    /** Names this has been called in the past */
    private List<String> oldNames;

    public Land()
    {
        oldNames = new LinkedList<String>();
    }

    public Land(World world, int x, int y, int z, int size)
    {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.x_start = x - size;
        this.y_start = y - size;
        this.z_start = z - size;
        this.x_end = x + size;
        this.y_end = y + size;
        this.z_end = z + size;
    }

    @Override
    public void init()
    {
        super.init();
        if (getID() == null)
            newID();
    }

    @Override
    public UUID getID()
    {
        return id;
    }

    @Override
    public Land setID(UUID id)
    {
        this.id = id;
        return this;
    }

    /** Creates a new id for this land */
    protected void newID()
    {
        id = UUID.randomUUID();
    }

    /** Sets the name of the region of land */
    public Land setName(String name)
    {
        this.name = name;
        return this;
    }

    /** Name of the region of land */
    public String getName()
    {
        return name;
    }

    /** Changes the name of the land */
    public Land rename(String newName)
    {
        if (!oldNames.contains(getName()))
            oldNames.add(getName());
        return this.setName(newName);
    }

    @Override
    public boolean isValid()
    {
        return !isInvalid && world != null;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        super.save(nbt);
        nbt.setString("name", this.name);
        nbt.setString("landid", this.id.toString());
        if (this.oldNames != null && !this.oldNames.isEmpty())
        {
            NBTTagCompound oldNames = new NBTTagCompound();
            oldNames.setInteger("count", this.oldNames.size());
            for (int i = 0; i < this.oldNames.size(); i++)
            {
                oldNames.setString("name" + i, this.oldNames.get(i));
            }
            nbt.setCompoundTag("oldnames", oldNames);
        }
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.load(nbt);
        this.name = nbt.getString("name");
        this.id = UUID.fromString(nbt.getString("landid"));
        if (nbt.hasKey("oldnames"))
        {
            NBTTagCompound oldNames = nbt.getCompoundTag("oldnames");
            oldNames.setInteger("count", this.oldNames.size());
            for (int i = 0; i < oldNames.getInteger("count"); i++)
            {
                this.oldNames.add(oldNames.getString("name" + i));
            }
        }
    }

   

}
