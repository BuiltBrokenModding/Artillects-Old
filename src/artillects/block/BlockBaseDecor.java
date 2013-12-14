package artillects.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.Artillects;

public class BlockBaseDecor extends BlockBase {
	
	private boolean lightPos = true;
	
	public BlockBaseDecor(String name) {
		super(name, Material.rock);
		setTextureName(Artillects.PREFIX + name);
	}
}
