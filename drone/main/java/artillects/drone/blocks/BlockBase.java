package artillects.drone.blocks;

import net.minecraft.block.material.Material;
import artillects.core.ArtillectsTab;
import artillects.core.Reference;

public class BlockBase extends resonant.lib.prefab.block.BlockAdvanced implements IHiveBlock
{

    public BlockBase(int id, String name, Material material)
    {
        super(id, material);
        this.setUnlocalizedName(Reference.PREFIX + name);
        this.setTextureName(Reference.PREFIX + name);
        this.setCreativeTab(ArtillectsTab.instance());
    }

}
