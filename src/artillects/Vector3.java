package artillects;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Vector3 extends Vector2
{
    public double z;

    public Vector3(double x, double y, double z)
    {
        super(x, y);
        this.z = z;
    }

    public Vector3(Entity entity)
    {
        super(entity.posX, entity.posY);
        this.z = entity.posZ;
    }

    public Vector3(TileEntity tileentity)
    {
        super(tileentity.xCoord, tileentity.yCoord);
        this.z = tileentity.zCoord;
    }

    public Vector3()
    {
        super(0, 0);
        this.z = 0;
    }

    public Vector3(NBTTagCompound nbt)
    {
        super(nbt.getDouble("x"), nbt.getDouble("y"));
        this.z = nbt.getDouble("x");
    }

    public Vector3 add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 add(Vector3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    public Vector3 add(double par1)
    {
        this.x += par1;
        this.y += par1;
        this.z += par1;
        return this;
    }

    public Vector3 subtract(Vector3 par1)
    {
        this.x -= par1.x;
        this.y -= par1.y;
        this.z -= par1.z;
        return this;
    }

    public Vector3 subtract(double par1)
    {
        this.x -= par1;
        this.y -= par1;
        this.z -= par1;
        return this;
    }

    public double distance(Vector3 compare)
    {
        Vector3 difference = this.clone().subtract(compare);
        return difference.getMagnitude();
    }

    public double distance(Entity entity)
    {
        return this.distance(new Vector3(entity));
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public double getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public Vector3 clone()
    {
        return new Vector3(x, y, z);
    }

    public void setBlock(World world, int i, int j)
    {
        world.setBlock((int) x, (int) y, (int) z, i, j, 3);
    }

    public Vec3 toVec3()
    {
        return Vec3.createVectorHelper(x, y, z);
    }

    @Override
    public int hashCode()
    {
        return ("X:" + this.x + "Y:" + this.y + "Z:" + this.z).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Vector3)
        {
            Vector3 vector3 = (Vector3) o;
            return this.x == vector3.x && this.y == vector3.y && this.z == vector3.z;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]";
    }

    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setDouble("x", this.x);
        nbt.setDouble("y", this.y);
        nbt.setDouble("z", this.z);
        return nbt;
    }

    public int getBlockID(World world)
    {
        return world.getBlockId((int) x, (int) y, (int) z);
    }
    
    public int getBlockMeta(World world)
    {
        return world.getBlockMetadata((int) x, (int) y, (int) z);
    }

}
