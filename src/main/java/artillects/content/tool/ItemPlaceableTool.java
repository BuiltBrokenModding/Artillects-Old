package artillects.content.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import resonant.lib.render.EnumColor;
import resonant.lib.utility.LanguageUtility;

import java.util.List;

public class ItemPlaceableTool extends ItemBlock
{
    public ItemPlaceableTool(Block block)
    {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
    {
        String tooltipSmall = LanguageUtility.getLocal(getUnlocalizedName(itemStack) + ".tooltip.small");
        String tooltip = LanguageUtility.getLocal(getUnlocalizedName(itemStack) + ".tooltip.large");

        if (tooltip != null && tooltip.length() > 0)
        {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                if (tooltipSmall != null)
                    list.addAll(LanguageUtility.splitStringPerWord(tooltipSmall, 5));
                list.add(LanguageUtility.getLocal("tooltip.noShift").replace("%0", EnumColor.AQUA.toString()).replace("%1", EnumColor.GREY.toString()));
            }
            else
            {
                list.add(LanguageUtility.getLocal("tool.use.info"));
                list.add(LanguageUtility.getLocal("tool.place.info"));
                if (tooltip != null)
                    list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking())
        {
            return used(player, world, x, y, z, side);
        }
        else
        {
            return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }

    public boolean used(EntityPlayer player, World world, int x, int y, int z, int side)
    {
        return false;
    }
}
