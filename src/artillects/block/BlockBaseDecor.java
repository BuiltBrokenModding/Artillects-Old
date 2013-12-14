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
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3) {
		if(player.isSneaking()) {
			return false;
		}
		
		if(world.isRemote) {
			if(this == Artillects.blockLight) {
				if(lightPos == false) {
					this.setLightValue(1F);
					lightPos = true;
					world.markBlockForRenderUpdate(x, y, z);
					return true;
				}
				if(lightPos == true) {
					this.setLightValue(0F);
					lightPos = false;
					world.markBlockForRenderUpdate(x, y, z);
					return true;
				}
				System.out.println("UPDATED!");
			}
		}
		return false;
	}
}
