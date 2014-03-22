package artillects.drone.items;

import java.util.List;

import calclavia.lib.schematic.SchematicMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.api.vector.VectorWorld;
import artillects.core.Reference;
import artillects.core.building.EnumStructurePeaces;
import artillects.drone.hive.HiveComplex;

public class ItemBuildingGenerator extends ItemBase
{
    public ItemBuildingGenerator(int id)
    {
        super(id, "buildingGenerator");
        this.setHasSubtypes(true);
        this.setTextureName(Reference.PREFIX + "buildingGenerator");
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
                    SchematicMap schematic = EnumStructurePeaces.values()[itemStack.getItemDamage()].getSchematic();
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
