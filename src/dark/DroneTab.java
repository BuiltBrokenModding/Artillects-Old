package dark;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DroneTab extends CreativeTabs
{
    private static DroneTab instance;
    private static ItemStack itemStack;

    public DroneTab()
    {
        super("Drones");
    }

    public static DroneTab instance()
    {
        if (instance == null)
        {
            instance = new DroneTab();
        }
        return instance;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if (itemStack == null)
        {
            itemStack = new ItemStack(Item.ingotIron);
        }
        return DroneTab.itemStack;
    }

    public static void setIconItemStack(ItemStack stack)
    {
        DroneTab.itemStack = stack;
    }

}
