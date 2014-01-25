package artillects.block.door;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import artillects.Artillects;
import artillects.block.BlockBase;
import artillects.block.IHiveBlock;

public class BlockDoorFrame extends BlockBase implements IHiveBlock {

	Icon[] icons = new Icon[2];
	
	public BlockDoorFrame() {
		super("doorFrame", Material.rock);
		setHardness(16F);
		setResistance(1000F);
	}
	
	public Icon getIcon(int side, int metadata) {
		if(side == 1 || side == 0) {
			return icons[0];
		}
		return icons[1];
	}
	
	public void registerIcons(IconRegister ir) {
		icons[0] = ir.registerIcon(Artillects.PREFIX + "decorWall2");
		icons[1] = ir.registerIcon(Artillects.PREFIX + "doorFrame_open");
	}
}
