package com.builtbroken.artillects.core.entity.passive;

import com.builtbroken.mc.prefab.inventory.BasicInventory;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/2/2016.
 */
public class InventoryNPC extends BasicInventory
{
    /** Main gear Slot 0 is held item. Slot 1-4 is armor. */
    public ItemStack[] gear = new ItemStack[5];

    public InventoryNPC()
    {
        super(10);
    }
}
