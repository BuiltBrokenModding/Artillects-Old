package artillects.block;

import artillects.Artillects;
import net.minecraft.block.material.Material;

public class BlockDecWall extends BlockBase {

	public BlockDecWall() {
		super("decorWall", Material.rock);
		setTextureName(Artillects.PREFIX + "decorWall");
	}

}
