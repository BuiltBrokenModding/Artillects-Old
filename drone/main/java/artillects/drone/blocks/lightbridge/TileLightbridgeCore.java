package artillects.drone.blocks.lightbridge;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import artillects.drone.Drone;
import calclavia.lib.java.Triple;
import calclavia.lib.prefab.tile.TileAdvanced;

public class TileLightbridgeCore extends TileAdvanced
{

    public TileLightbridgeCore pair;
    public boolean toggle = true;

    public void validate()
    {
        super.validate();
    }

    public void invalidate()
    {
        if (this.pair != null && this.pair.pair == this)
        {
            System.out.println("Core: " + pair.pair.toString() + ", has lost connection with: " + this.toString());
            this.pair.pair = null;
            this.pair = null;
        }
        super.invalidate();
    }

    public void setPair(TileLightbridgeCore toPair)
    {
        if (!worldObj.isRemote)
        {
            if (toPair.pair == null && this.pair == null)
            {
                pair = toPair;
                toPair.pair = this;
                System.out.println("Core: " + this.toString() + ", was paired with: " + this.pair.toString());
            }
        }
    }

    public void toggle()
    {
        System.out.println("Toggled? = " + toggle);
        if (!toggle)
        {
            toggle = true;
            if (pair != null && pair.pair == this)
            {
                pair.toggle = true;
            }
        }
        else
        {
            toggle = false;
            if (pair != null && pair.pair == this)
            {
                pair.toggle = false;
            }
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            if (checkForLightbridge() != null)
            {
                ForgeDirection dir = checkForLightbridge().getA();
                Vector3 vec = checkForLightbridge().getB();
                TileLightbridgeCore core = checkForLightbridge().getC();

                if (pair != core)
                    if (worldObj.getWorldTime() % 5 == 0)
                        setPair(core);
                if (toggle)
                {
                    if (pair != null)
                    {
                        for (int i = 1; i < Vector3.distance(new Vector3(this), vec); i++)
                        {
                            if (dir == ForgeDirection.NORTH)
                            {
                                if (worldObj.getBlockId(xCoord, yCoord, zCoord + i) == 0)
                                {
                                    worldObj.setBlock(xCoord, yCoord, zCoord + i, Drone.blockLightbridge.blockID);
                                }
                            }
                            if (dir == ForgeDirection.SOUTH)
                            {
                                if (worldObj.getBlockId(xCoord, yCoord, zCoord - i) == 0)
                                {
                                    worldObj.setBlock(xCoord, yCoord, zCoord - i, Drone.blockLightbridge.blockID);
                                }
                            }
                            if (dir == ForgeDirection.EAST)
                            {
                                if (worldObj.getBlockId(xCoord + i, yCoord, zCoord) == 0)
                                {
                                    worldObj.setBlock(xCoord + i, yCoord, zCoord, Drone.blockLightbridge.blockID);
                                }
                            }
                            if (dir == ForgeDirection.WEST)
                            {
                                if (worldObj.getBlockId(xCoord - i, yCoord, zCoord) == 0)
                                {
                                    worldObj.setBlock(xCoord - i, yCoord, zCoord, Drone.blockLightbridge.blockID);
                                }
                            }
                        }
                    }
                    else
                    {
                        forceDisable(dir, vec);
                    }
                }
                else
                {
                    forceDisable(dir, vec);
                    pair.forceDisable(dir.getOpposite(), new Vector3(this));
                }
            }
        }
    }

    public void forceDisable(ForgeDirection dir, Vector3 vec)
    {
        System.out.println("FORCE DISABLE");
        for (int i = 1; i < Vector3.distance(new Vector3(this), vec); i++)
        {
            if (dir == ForgeDirection.NORTH)
            {
                if (worldObj.getBlockId(xCoord, yCoord, zCoord + i) == Drone.blockLightbridge.blockID)
                {
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord + i);
                }
            }
            if (dir == ForgeDirection.SOUTH)
            {
                if (worldObj.getBlockId(xCoord, yCoord, zCoord - i) == Drone.blockLightbridge.blockID)
                {
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord - i);
                }
            }
            if (dir == ForgeDirection.EAST)
            {
                if (worldObj.getBlockId(xCoord + i, yCoord, zCoord) == Drone.blockLightbridge.blockID)
                {
                    worldObj.setBlockToAir(xCoord + i, yCoord, zCoord);
                }
            }
            if (dir == ForgeDirection.WEST)
            {
                if (worldObj.getBlockId(xCoord - i, yCoord, zCoord) == Drone.blockLightbridge.blockID)
                {
                    worldObj.setBlockToAir(xCoord - i, yCoord, zCoord);
                }
            }
        }
    }

    public Triple<ForgeDirection, Vector3, TileLightbridgeCore> checkForLightbridge()
    {

        for (int n = 2; n < 50; n++)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + n);
            if (tile instanceof TileLightbridgeCore)
            {
                if (((TileLightbridgeCore) tile).pair == null && this.pair == null)
                {
                    Triple<ForgeDirection, Vector3, TileLightbridgeCore> map = new Triple<ForgeDirection, Vector3, TileLightbridgeCore>(ForgeDirection.NORTH, new Vector3(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + n)), (TileLightbridgeCore) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + n));
                    return map;
                }
            }
        }
        for (int s = 2; s < 50; s++)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - s);
            if (tile instanceof TileLightbridgeCore)
            {
                if (((TileLightbridgeCore) tile).pair == null && this.pair == null)
                {
                    Triple<ForgeDirection, Vector3, TileLightbridgeCore> map = new Triple<ForgeDirection, Vector3, TileLightbridgeCore>(ForgeDirection.SOUTH, new Vector3(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - s)), (TileLightbridgeCore) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - s));
                    return map;
                }
            }
        }
        for (int e = 2; e < 50; e++)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + e, yCoord, zCoord);
            if (tile instanceof TileLightbridgeCore)
            {
                if (((TileLightbridgeCore) tile).pair == null && this.pair == null)
                {
                    Triple<ForgeDirection, Vector3, TileLightbridgeCore> map = new Triple<ForgeDirection, Vector3, TileLightbridgeCore>(ForgeDirection.EAST, new Vector3(worldObj.getBlockTileEntity(xCoord + e, yCoord, zCoord)), (TileLightbridgeCore) worldObj.getBlockTileEntity(xCoord + e, yCoord, zCoord));
                    return map;
                }
            }
        }
        for (int w = 2; w < 50; w++)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord - w, yCoord, zCoord);
            if (tile instanceof TileLightbridgeCore)
            {
                if (((TileLightbridgeCore) tile).pair == null && this.pair == null)
                {
                    Triple<ForgeDirection, Vector3, TileLightbridgeCore> map = new Triple<ForgeDirection, Vector3, TileLightbridgeCore>(ForgeDirection.WEST, new Vector3(worldObj.getBlockTileEntity(xCoord - w, yCoord, zCoord)), (TileLightbridgeCore) worldObj.getBlockTileEntity(xCoord - w, yCoord, zCoord));
                    return map;
                }
            }
        }
        return null;
    }

    public String toString()
    {
        return pair == null ? "LBCore@(NULL)" : "LBCore@(" + this.xCoord + "," + this.yCoord + "," + this.zCoord + ")";

    }
}
