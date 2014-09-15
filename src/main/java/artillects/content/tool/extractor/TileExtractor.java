package artillects.content.tool.extractor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import resonant.api.IExternalInventory;
import resonant.api.IInventoryProvider;
import resonant.lib.utility.inventory.ExternalInventory;
import resonant.lib.utility.inventory.InventoryUtility;
import artillects.content.tool.TilePlaceableTool;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.transform.rotation.EulerAngle;
import universalelectricity.core.transform.vector.Vector3;

/** Used to pick up blocks without mining them. Has the option to exact only one block type. As well
 * has the option to extract in an area.
 * 
 * @author Darkguardsman */
public class TileExtractor extends TilePlaceableTool implements IInventoryProvider, ISidedInventory
{
    public static int MAX_RANGE = 4;
    public static int COOLDOWN = 100;
    protected int range = 1;
    protected int cooldownTicks = 0;
    protected IExternalInventory inventory;

    public TileExtractor()
    {
        super(Material.anvil);
        itemBlock(ItemExtractor.class);
        isOpaqueCube(false);
        normalRender(false);
        customItemRender(true);
        this.doRayTrace = true;
        this.rayDistance = 10;
    }

    @Override
    public IExternalInventory getInventory()
    {
        if (inventory == null)
        {
            inventory = new ExternalInventory(this, 4);
        }
        return inventory;
    }

    @Override
    public void update()
    {
        super.update();
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
        angle.yaw_$eq(EulerAngle.clampAngleTo360(angle.yaw()));
        angle.pitch_$eq(EulerAngle.clampAngleTo360(angle.pitch()));

        if (this.loc == null)
            loc = new Vector3(this).add(offset);

        MovingObjectPosition hit = getRayHit();
        if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
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
            Block block = h.getBlock(world);
            if (block != null)
            {
                ArrayList<ItemStack> drops = block.getDrops(world, h.xi(), h.yi(), h.zi(), h.getBlockMetadata(world), 0);
                h.setBlockToAir(world);
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
        //TODO onInventoryChanged();
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
        //TODO replace with helper method for getting radius
        for (int x = hit.xi() - dx; x <= hit.xi() + dx; x++)
        {
            for (int y = hit.yi() - dy; y <= hit.yi() + dy; y++)
            {
                for (int z = hit.zi() - dz; z <= hit.zi() + dz; z++)
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
        return canGrab(world, vec.xi(), vec.yi(), vec.zi());
    }

    public static boolean canGrab(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block != null && block.getBlockHardness(world, x, y, z) >= 0 && !block.isAir(world, x, y, z) && world.getTileEntity(x, y, z) == null)
        {
            ArrayList<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
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
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
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
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

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
