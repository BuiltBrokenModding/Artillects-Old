package artillects.drone.blocks.lightbridge;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import artillects.drone.blocks.BlockBase;
import artillects.drone.blocks.IHiveBlock;

public class BlockLightbridge extends BlockBase implements ITileEntityProvider, IHiveBlock {

	public BlockLightbridge(int id) {
		super(id, "lightbridge", Material.ice);
		setBlockBounds(0F, 0.8F, 0F, 1F, 1F, 1F);
		setLightValue(1.0F);
		setBlockUnbreakable();
		setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileLightbridge();
	}
	
	@Override
    public int getRenderType() {
    	return -1;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    	return false;
    }
    
}
