package artillects.item;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.Vector3;
import artillects.hive.HiveComplex;
import artillects.hive.schematics.Schematic;

public class ItemBuildingTest extends ItemBase
{
    public ItemBuildingTest()
    {
        super("BuildingTest");
        this.setHasSubtypes(true);
        this.setTextureName(Artillects.PREFIX + "BuildingTest");
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (!world.isRemote && itemStack != null && itemStack.getItemDamage() < Building.values().length)
        {
            if (itemStack.getItemDamage() == 0)
            {
                HiveComplex complex = new HiveComplex("TestHive", new Vector3(x, y, z));
                complex.loadTunnelTest();
            }
            Schematic schematic = Building.values()[itemStack.getItemDamage()].getSchematic();
            if (schematic != null && schematic.blocks != null)
            {
                for (Entry<Vector3, int[]> entry : schematic.blocks.entrySet())
                {
                    if (entry.getValue()[0] != Block.sponge.blockID)
                    {
                        Vector3 setPos = new Vector3(x, y, z).subtract(schematic.schematicCenter).add(entry.getKey());
                        setPos.setBlock(world, entry.getValue()[0], entry.getValue()[1]);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (itemStack != null && itemStack.getItemDamage() < Building.values().length)
        {
            par3List.add("Generates a building");
            par3List.add("Building: " + Building.values()[itemStack.getItemDamage()]);
        }
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (Building building : Building.values())
        {
            par3List.add(new ItemStack(this, 1, building.ordinal()));
        }

    }

    public static enum Building
    {
        TEST("Test"),
        TUNNELZ("ZTunnel"),
        TUNNELX("XTunnel"),
        TUNNELY("YTunnel"),
        TUNNELC("CTunnel");
        public String name;
        public Schematic schematic;

        private Building(String name)
        {
            this.name = name;
        }

        public Schematic getSchematic()
        {
            if (schematic == null)
            {
                schematic = new Schematic();
                schematic.getFromResourceFolder(name);
            }
            return schematic;
        }
    }
}
