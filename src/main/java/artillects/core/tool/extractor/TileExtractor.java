package artillects.core.tool.extractor;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import artillects.core.tool.TilePlaceableTool;

/** Used to pick up blocks without mining them. Has the option to exact only one block type. As well
 * has the option to extract in an area.
 * 
 * @author Darkguardsman */
public class TileExtractor extends TilePlaceableTool
{
    public static int MAX_WIDTH = 4;
    public static int MAX_HEIGHT = 4;

    protected int width = 1;
    protected int height = 1;
    protected ItemStack extractStack = null;

    public TileExtractor()
    {
        super(UniversalElectricity.machine);
    }

    public List<Vector3> getBlocksHit()
    {
        return getBlocksHit(this.lastRayHit, getDirection());
    }

    public static List<Vector3> getBlocksHit(Vector3 hit, ForgeDirection side)
    {
        return null;
    }
}
