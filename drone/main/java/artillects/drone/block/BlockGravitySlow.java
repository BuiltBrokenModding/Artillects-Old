package artillects.drone.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import artillects.core.Reference;

public class BlockGravitySlow extends BlockBase implements IHiveBlock
{

    public BlockGravitySlow(int id)
    {
        super(id, "gravitySlow", Material.rock);
        setTextureName(Reference.PREFIX + "gravitySlow");
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
}
