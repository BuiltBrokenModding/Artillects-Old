package dark;

import net.minecraft.entity.Entity;

public class Vector3 extends Vector2
{
    double z;

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
}
