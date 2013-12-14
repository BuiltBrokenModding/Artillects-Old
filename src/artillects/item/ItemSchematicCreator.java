package artillects.item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import artillects.Vector3;
import artillects.hive.schematics.Schematic;

public class ItemSchematicCreator extends ItemBase
{
    static HashMap<String, Vector3[]> playerPointSelection = new HashMap<String, Vector3[]>();
    static HashMap<String, Schematic> playerSchematic = new HashMap<String, Schematic>();

    public ItemSchematicCreator()
    {
        super("schematicCreator");
        this.setHasSubtypes(true);
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
        player.sendChatToPlayer(ChatMessageComponent.createFromText("Click"));
        String user = player.username;
        Vector3 pos = null;
        Vector3 pos2 = null;
        Schematic schematic = null;
        if (playerPointSelection.containsKey(user) && playerPointSelection.get(user) != null)
        {
            pos = playerPointSelection.get(user)[0];
            pos2 = playerPointSelection.get(user)[1];
        }
        if (pos == null)
        {
            pos = new Vector3(x, y, z);
            player.sendChatToPlayer(ChatMessageComponent.createFromText("Pos one set to " + pos.toString()));
        }
        else if (pos2 == null)
        {
            pos2 = new Vector3(x, y, z);
            player.sendChatToPlayer(ChatMessageComponent.createFromText("Pos2 one set to " + pos2.toString()));
        }
        if (pos != null && pos2 != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh.mm.ss");
            String name = user + "_Schematic_" + dateFormat.format(new Date());
            schematic = this.loadWorldSelection(world, pos, pos2);

            player.sendChatToPlayer(ChatMessageComponent.createFromText("Saved Schematic  " + name));
            pos = null;
            pos2 = null;
            playerSchematic.put(user, schematic);
            schematic.saveToBaseDirectory(name);
        }
        playerPointSelection.put(user, new Vector3[] { pos, pos2 });

        return true;
    }

    public Schematic loadWorldSelection(World world, Vector3 pos, Vector3 pos2)
    {
        int deltaX, deltaY, deltaZ;
        Vector3 start = new Vector3(pos.x > pos2.x ? pos2.x : pos.x, pos.y > pos2.y ? pos2.y : pos.y, pos.z > pos2.z ? pos2.z : pos.z);

        Schematic sch = new Schematic();
        if (pos.x < pos2.x)
        {
            deltaX = (int) (pos2.x - pos.x + 1);
        }
        else
        {
            deltaX = (int) (pos.x - pos2.x + 1);
        }
        if (pos.y < pos2.y)
        {
            deltaY = (int) (pos2.y - pos.y + 1);
        }
        else
        {
            deltaY = (int) (pos.y - pos2.y + 1);
        }
        if (pos.z < pos2.z)
        {
            deltaZ = (int) (pos2.z - pos.z + 1);
        }
        else
        {
            deltaZ = (int) (pos.z - pos2.z + 1);
        }
        sch.schematicSize = new Vector3(deltaX, deltaY, deltaZ);
        for (int x = 0; x < deltaX; ++x)
        {
            for (int y = 0; y < deltaY; ++y)
            {
                for (int z = 0; z < deltaZ; ++z)
                {
                    int blockID = world.getBlockId((int) start.x + x, (int) start.y + y, (int) start.z + z);
                    int blockMeta = world.getBlockMetadata((int) start.x + x, (int) start.y + y, (int) start.z + z);
                    sch.blocks.put(new Vector3(x, y, z), new int[] { blockID, blockMeta });
                }
            }
        }
        return sch;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this.itemID, 1, 0));

    }

}
