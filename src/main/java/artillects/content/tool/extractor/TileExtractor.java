package artillects.content.tool.extractor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import resonant.api.IExternalInventory;
import resonant.api.IExternalInventoryBox;
import resonant.lib.utility.inventory.ExternalInventory;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.EulerAngle;
import universalelectricity.api.vector.Vector3;
import artillects.content.tool.TilePlaceableTool;

/** Used to pick up blocks without mining them. Has the option to exact only one block type. As well
 * has the option to extract in an area.
 * 
 * @author Darkguardsman */
public class TileExtractor extends TilePlaceableTool implements IExternalInventory, ISidedInventory
{
    public static int MAX_RANGE = 4;
    public static int COOLDOWN = 100;
    protected int range = 1;
    protected int cooldownTicks = 0;
    protected IExternalInventoryBox inventory;

    public TileExtractor()
    {
        super(UniversalElectricity.machine);
        itemBlock = ItemExtractor.class;
        isOpaqueCube = false;
        normalRender = false;
        customItemRender = true;
        this.doRayTrace = true;
        this.rayDistance = 10;
    }

    @Override
    public IExternalInventoryBox getInventory()
    {
        if (inventory == null)
        {
            inventory = new ExternalInventory(this, 4);
        }
        return inventory;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (cooldownTicks > 0)
            cooldownTicks--;
        
        if (world().isBlockIndirectlyGettingPowered(x(), y(), z()) && cooldownTicks <= 0)
        {
            cooldownTicks = COOLDOWN;
            extractBlocks();
            //TODO drain power
        }
    }
    
    public void doRayTrace()
    {
        lastRayHit = null;
        sideHit = null;
        angle.yaw = EulerAngle.clampAngleTo360(angle.yaw);
        angle.pitch = EulerAngle.clampAngleTo360(angle.pitch);

        if (this.loc == null)
            loc = new Vector3(this).translate(offset);

        MovingObjectPosition hit = getRayHit();
        if (hit != null && hit.typeOfHit == EnumMovingObjectType.TILE)
        {
            lastRayHit = new Vector3(hit.blockX, hit.blockY, hit.blockZ);
            sideHit = ForgeDirection.getOrientation(hit.sideHit);
        }
    }

    public void extractBlocks()
    {
        extractBlocks(this, world(), lastRayHit, sideHit, range);
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
                h.setBlock(world, 0);
                for (ItemStack stack : drops)
                {
                    for (int slot = 0; slot < inv.getSizeInventory() - (inv instanceof InventoryPlayer ? 4 : 0); slot++)
                    {
                        ItemStack slotStack = inv.getStackInSlot(slot);
                        if (slotStack == null)
                        {
                            inv.setInventorySlotContents(slot, stack);
                            stack = null;
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
                                    break;
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
                    if (stack != null && stack.stackSize > 0)
                    {
                        InventoryUtility.dropItemStack(world, h, stack);
                    }
                }
            }
        }
        inv.onInventoryChanged();
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
        if (block != null && block.getBlockHardness(world, x, y, z) >= 0 && !block.isAirBlock(world, x, y, z) && world.getBlockTileEntity(x, y, z) == null)
        {
            ArrayList<ItemStack> drops = block.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            if (drops != null & !drops.isEmpty())
                return true;
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return this.getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return this.getInventory().getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return this.getInventory().decrStackSize(i, j);
    }

    public void incrStackSize(int slot, ItemStack itemStack)
    {
        if (this.getStackInSlot(slot) == null)
        {
            setInventorySlotContents(slot, itemStack.copy());
        }
        else if (this.getStackInSlot(slot).isItemEqual(itemStack))
        {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return this.getInventory().getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        this.getInventory().setInventorySlotContents(i, itemStack);
    }

    @Override
    public String getInvName()
    {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return this.getInventory().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.getInventory().isUseableByPlayer(entityplayer);
    }

    @Override
    public void openChest()
    {
        this.getInventory().openChest();

    }

    @Override
    public void closeChest()
    {
        this.getInventory().closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return this.getInventory().isItemValidForSlot(i, itemstack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return this.getInventory().getAccessibleSlotsFromSide(var1);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return this.getInventory().canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return this.getInventory().canExtractItem(i, itemstack, j);
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side)
    {
        return false;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side)
    {
        if (slot >= this.getSizeInventory())
        {
            return false;
        }
        return true;
    }
}
