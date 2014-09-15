package artillects.content.blocks.door;

import artillects.core.Artillects;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemLockedDoor extends ItemDoor
{
    public ItemLockedDoor()
    {
        super(null);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        if (side == 1)
        {
            ++y;
            Block block = Artillects.blockLockedDoor;

            if (block instanceof BlockLockedDoor)
            {
                if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
                {
                    if (!block.canPlaceBlockAt(world, x, y, z))
                    {
                        return false;
                    }
                    else
                    {
                        int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                        placeDoorBlock(world, x, y, z, i1, block);

                        TileLockedDoor door = ((BlockLockedDoor) block).getDoorTile(world, x, y, z);
                        if (door != null)
                        {
                            door.onCreated(stack, player);
                        }
                        --stack.stackSize;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
