package artillects.content.items;

import artillects.core.Reference;
import artillects.core.commands.PlayerSelectionHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.List;

public class ItemSchematicCreator extends Item
{

    public ItemSchematicCreator()
    {
        super();
        this.setHasSubtypes(true);
        this.setTextureName(Reference.PREFIX + "schematicCreator");
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (itemStack != null)
        {
            if (itemStack.getItemDamage() == 0)
            {
                par3List.add("Generates a schematic");
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (!world.isRemote)
        {
            if (!player.isSneaking())
            {
                if (!PlayerSelectionHandler.hasPointOne(player))
                {
                    PlayerSelectionHandler.setPointOne(player, new VectorWorld(world, x, y, z));
                }
                else if (!PlayerSelectionHandler.hasPointTwo(player))
                {
                    PlayerSelectionHandler.setPointTwo(player, new VectorWorld(world, x, y, z));
                }
            }
            else
            {
                player.addChatComponentMessage(new ChatComponentText("Selection cleared"));
                PlayerSelectionHandler.setPointOne(player, null);
                PlayerSelectionHandler.setPointTwo(player, null);
            }
        }
        return true;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

}
