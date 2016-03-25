package com.builtbroken.artillects.core.integration;

import com.builtbroken.artillects.core.integration.api.IUsageInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public abstract class UsageInventory<T extends TileEntity> extends UsageTile<T> implements IUsageInventory<T>
{
    public UsageInventory(String localization, String id)
    {
        super(localization, "inventory." + id);
    }

    @Override
    public boolean doesApplyTo(TileEntity tile)
    {
        return tile instanceof IInventory;
    }

}
