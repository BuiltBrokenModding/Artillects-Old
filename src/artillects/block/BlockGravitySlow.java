package artillects.block;

import artillects.Artillects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockGravitySlow extends BlockBase {

	public BlockGravitySlow() {
		super("gravitySlow", Material.rock);
		setTextureName(Artillects.PREFIX + "gravitySlow");
	}
	
    public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
    	entity.motionX *= 0.4D;
    	entity.motionZ *= 0.4D;
    }
}
