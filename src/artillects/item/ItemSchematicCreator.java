package artillects.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.VectorWorld;
import artillects.commands.PlayerSelectionHandler;

public class ItemSchematicCreator extends ItemBase
{

    public ItemSchematicCreator()
    {
        super("schematicCreator");
        this.setHasSubtypes(true);
        this.setTextureName(Artillects.PREFIX + "schematicCreator");
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
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Selection cleared"));
                PlayerSelectionHandler.setPointOne(player, null);
                PlayerSelectionHandler.setPointTwo(player, null);
            }
        }
        return true;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this.itemID, 1, 0));
    }

}
