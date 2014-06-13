package artillects.drone.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import resonant.lib.schematic.BlockMap;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.building.EnumStructurePeaces;
import artillects.drone.hive.HiveComplex;

public class ItemBuildingGenerator extends Item
{
    public ItemBuildingGenerator(int id)
    {
        super(id);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (!world.isRemote && itemStack != null && itemStack.getItemDamage() < EnumStructurePeaces.values().length)
        {
            if (itemStack.getItemDamage() == 0)
            {
                HiveComplex complex = new HiveComplex("PlayerGeneratedHive" + System.currentTimeMillis(), new VectorWorld(world, x, y, z));
                complex.loadGeneralBuilding(true);
            }
            else if (itemStack.getItemDamage() == 1)
            {
                HiveComplex complex = new HiveComplex("PlayerGeneratedHive" + System.currentTimeMillis(), new VectorWorld(world, x, y, z));
                complex.loadFabricatorDemo();
            }
            else
            {
                if (EnumStructurePeaces.values()[itemStack.getItemDamage()].makeTool)
                {
                    BlockMap schematic = EnumStructurePeaces.values()[itemStack.getItemDamage()].getSchematic();
                    schematic.build(new VectorWorld(world, x, y, z), false);
                }
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (itemStack != null && itemStack.getItemDamage() < EnumStructurePeaces.values().length)
        {
            if (EnumStructurePeaces.values()[itemStack.getItemDamage()].makeTool)
            {
                par3List.add("Generates a building");
                par3List.add("Building: " + EnumStructurePeaces.values()[itemStack.getItemDamage()].toolName);
            }
            else
            {
                par3List.add("Unusable");
            }
        }
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (EnumStructurePeaces building : EnumStructurePeaces.values())
        {
            if (building.makeTool)
                par3List.add(new ItemStack(this, 1, building.ordinal()));
        }

    }
}
