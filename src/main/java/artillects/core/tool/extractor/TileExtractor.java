package artillects.core.tool.extractor;

import java.util.ArrayList;
import java.util.List;

import resonant.lib.utility.inventory.InventoryUtility;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import artillects.core.tool.TilePlaceableTool;
import artillects.core.tool.surveyor.ItemSurveyor;

/** Used to pick up blocks without mining them. Has the option to exact only one block type. As well
 * has the option to extract in an area.
 * 
 * @author Darkguardsman */
public class TileExtractor extends TilePlaceableTool
{
    public static int MAX_RANGE = 4;
    protected int range = 1;

    public TileExtractor()
    {
        super(UniversalElectricity.machine);
        itemBlock = ItemExtractor.class;
    }

    public void extractBlocks()
    {

    }

    public static void extractBlocks(IInventory inv, World world, Vector3 hit, ForgeDirection side, int range)
    {
        List<Vector3> hits = getBlocksHit(world, hit, side, range);
        for (Vector3 h : hits)
        {
            int id = h.getBlockID(world);
            Block block = Block.blocksList[id];
            if (block != null)
            {
                ArrayList<ItemStack> drops = block.getBlockDropped(world, h.intX(), h.intY(), h.intZ(), h.getBlockMetadata(world), 0);
                for (ItemStack stack : drops)
                {
                    for (int slot = 0; slot < inv.getSizeInventory(); slot++)
                    {
                        ItemStack slotStack = inv.getStackInSlot(slot);
                        if (slotStack == null)
                        {
                            inv.setInventorySlotContents(slot, stack);
                            break;
                        }
                        else if (slotStack.isItemEqual(stack) && stack.getTagCompound() == null && slotStack.getTagCompound() == null)
                        {
                            if (slotStack.stackSize < slotStack.getMaxStackSize())
                            {
                                int space = slotStack.getMaxStackSize() - slotStack.stackSize;
                                if (stack.stackSize <= space)
                                {                                    
                                    slotStack.stackSize += stack.stackSize;
                                    inv.setInventorySlotContents(slot, slotStack);
                                    stack = null;
                                }
                                else
                                {
                                    slotStack.stackSize += space;
                                    stack.stackSize -= space;
                                    inv.setInventorySlotContents(slot, slotStack);
                                }
                            }
                        }
                    }
                    if(stack != null && stack.stackSize > 0)
                        InventoryUtility.dropItemStack(world, h, stack);
                }
            }
        }
    }

    public static List<Vector3> getBlocksHit(World world, Vector3 hit, ForgeDirection side, int range)
    {
        List<Vector3> list = new ArrayList<Vector3>();
        int dx, dy, dz;
        dx = dy = dz = range;
        switch (side)
        {
            case DOWN:
            case UP:
                dy = 0;
                break;
            case NORTH:
            case SOUTH:
                dz = 0;
                break;
            case EAST:
            case WEST:
                dx = 0;
                break;
        }

        for (int x = hit.intX() - dx; x <= hit.intX() + dx; x++)
        {
            for (int y = hit.intY() - dy; y <= hit.intY() + dy; y++)
            {
                for (int z = hit.intZ() - dz; z <= hit.intZ() + dz; z++)
                {
                    Vector3 vec = new Vector3(x, y, z);
                    if (canGrab(world, vec))
                        list.add(vec);
                }
            }
        }
        return list;
    }

    public static boolean canGrab(World world, Vector3 vec)
    {
        return canGrab(world, vec.intX(), vec.intY(), vec.intZ());
    }

    public static boolean canGrab(World world, int x, int y, int z)
    {
        int id = world.getBlockId(x, y, z);
        Block block = Block.blocksList[id];
        if (block != null && !block.isAirBlock(world, x, y, z) && world.getBlockTileEntity(x, y, z) == null)
        {
            ArrayList<ItemStack> drops = block.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            if (drops != null & !drops.isEmpty())
                return true;
        }
        return false;
    }
}
