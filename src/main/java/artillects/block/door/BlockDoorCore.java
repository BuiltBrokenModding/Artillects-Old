package artillects.block.door;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.block.BlockBase;
import artillects.block.IHiveBlock;

public class BlockDoorCore extends BlockBase implements IHiveBlock, ITileEntityProvider {

	public BlockDoorCore() {
		super("doorCore", Material.rock);
		setHardness(16F);
		setResistance(1000F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileDoorCore();
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f1, float f2, float f3) {
		return false;	
	}
}
