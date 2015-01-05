package artillects.content.blocks.door;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import com.builtbroken.lib.prefab.tile.TileAdvanced;

@SuppressWarnings("deprecation")
public class TileLockedDoor extends TileAdvanced
{
    public boolean isOpen = false;
    public boolean allowRedstone = false;
    public boolean autoLock = false;
    public boolean autoClose = false;

    private int closeTime = 7; // TODO add gui setting for this
    private int timeOpen = 0;
    private DoorType type = DoorType.NORMAL;

    public TileLockedDoor()
    {
        super(Material.rock);
    }

    @Override
    public void update()
    {
        super.update();
        if (this.ticks() % 20 == 0)
        {
            //TODO add upgrade to auto shut a door using a spring like item
            if (autoClose)
            {
                Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
                if (block instanceof BlockLockedDoor)
                {
                    isOpen = ((BlockLockedDoor) block).isDoorOpen(worldObj, xCoord, yCoord, zCoord);
                    if (isOpen && timeOpen++ >= closeTime)
                    {
                        isOpen = false;
                        ((BlockLockedDoor) block).activateDoor(worldObj, xCoord, yCoord, zCoord);
                    }
                    if (!isOpen)
                    {
                        timeOpen = 0;
                        this.isOpen = false;
                    }
                }
            }
        }
    }

    public void onCreated(ItemStack stack, Entity entity)
    {

    }
}
