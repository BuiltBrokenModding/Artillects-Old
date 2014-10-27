package artillects.core.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import resonant.lib.transform.vector.IVector2;
import resonant.lib.transform.vector.Vector2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/** 2D top down area of the map
 * 
 * @author Darkguardsman */
public class Land extends FactionObject
{
    private Plane area;

    /** Name of the region of land */
    private String name = "Land";

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
        area = new Plane(new Vector2(x - size, z - size), new Vector2(x + size, z + size));
    }

    protected String newID()
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(("" + 1 + this.x() + this.y() + this.z()).getBytes("UTF-8"));
            String id = hash.toString();
            System.out.println("ID: " + id);
            return id;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
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
        if (this.oldNames != null && !this.oldNames.isEmpty())
        {
            NBTTagCompound oldNames = new NBTTagCompound();
            oldNames.setInteger("count", this.oldNames.size());
            for (int i = 0; i < this.oldNames.size(); i++)
            {
                oldNames.setString("name" + i, this.oldNames.get(i));
            }
            nbt.setTag("oldnames", oldNames);
        }
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        this.load(nbt);
        this.name = nbt.getString("name");
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

    /** Does this land control this area */
    public boolean controls(IVector2 vec)
    {
        return this.area.contains(vec);
    }

}
