package artillects.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import artillects.Artillects;

public class BlockCommunication extends BlockBase {

	short textureMode = 0;
	
	public BlockCommunication() {
		super("decorCommunication", Material.rock);
		setTextureName(Artillects.PREFIX + "decorCom");
	}
}
