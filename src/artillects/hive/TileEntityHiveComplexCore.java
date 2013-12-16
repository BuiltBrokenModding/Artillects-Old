package artillects.hive;

import net.minecraft.nbt.NBTTagCompound;
import artillects.VectorWorld;
import artillects.tile.TileEntityAdvanced;

public class TileEntityHiveComplexCore extends TileEntityAdvanced
{
    protected HiveComplex complex;
    protected String complexName;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (complex == null)
        {
            complex = HiveComplexManager.instance().getClosestComplex(new VectorWorld(this), 30);
            if (complex == null)
            {
                complex = new HiveComplex(this);
            }
        }
        if (complex != null)
        {
            complex.updateEntity();
            if (complex.core != this)
            {
                complex.updateTileLink(this);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        //complexName = nbt.getString("complexName");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //nbt.setString("complexName", this.complexName);
    }
}
