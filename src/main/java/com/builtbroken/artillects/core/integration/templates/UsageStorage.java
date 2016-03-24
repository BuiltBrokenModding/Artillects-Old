package com.builtbroken.artillects.core.integration.templates;

import com.builtbroken.artillects.core.integration.UsageInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * Simple object used to generate a basic inventory usage template
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public class UsageStorage extends UsageInventory
{
    private final int[] slots;

    public UsageStorage(String localization, String id, int[] slots)
    {
        super(localization, id);
        this.slots = slots;
    }

    @Override
    public int[] getAccessableSlots(TileEntity tile)
    {
        return slots;
    }
}
