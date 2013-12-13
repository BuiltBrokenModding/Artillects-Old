package dark.drones.machines;

import net.minecraft.tileentity.TileEntity;

public class TileEntityAdvanced extends TileEntity
{
    protected long ticks = 0;

    protected void init()
    {

    }

    @Override
    public void updateEntity()
    {
        ticks++;
        if (ticks == 1)
        {
            this.init();
        }
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 0;
        }
    }
}
