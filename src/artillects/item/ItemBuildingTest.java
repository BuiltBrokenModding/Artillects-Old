package artillects.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import artillects.hive.schematics.Schematic;

public class ItemBuildingTest extends ItemBase
{
    public ItemBuildingTest()
    {
        super("BuildingTest");
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
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        return "Building";
    }

    public static enum Building
    {
        TEST("Test");
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
