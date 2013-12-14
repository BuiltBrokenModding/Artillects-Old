package artillects;

import net.minecraft.entity.Entity;
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

    public Vector3()
    {
        super(0, 0);
        this.z = 0;
    }

    public Vector3 add(Vector3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    public Vector3 add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
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

}
