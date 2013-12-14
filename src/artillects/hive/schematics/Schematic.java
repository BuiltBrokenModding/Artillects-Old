package artillects.hive.schematics;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import artillects.Vector3;
import artillects.VectorWorld;
import artillects.hive.ISaveObject;

/** File that represents all the data loaded from a schematic data file
 * 
 * @author Dark */
public class Schematic implements ISaveObject
{
    public static final String BlockList = "BlockList";

    protected String name;
    public Vector3 schematicSize;

    public Vector3 schematicCenter;

    public HashMap<Vector3, int[]> blocks = new HashMap<Vector3, int[]>();

    public boolean init = false;

    public String getFileName()
    {
        return this.name;
    }

    public void init()
    {
        if (this.schematicSize != null)
        {
            this.init = true;
            this.schematicCenter = new Vector3();
            this.schematicCenter.x = this.schematicSize.x / 2;
            //this.schematicCenter.y = this.schematicSize.y / 2;
            this.schematicCenter.z = this.schematicSize.z / 2;
        }
    }

    public void build(VectorWorld spot)
    {
        if (this.blocks != null)
        {
            for (Entry<Vector3, int[]> entry : this.blocks.entrySet())
            {
                if (entry.getValue()[0] != Block.sponge.blockID)
                {
                    Vector3 setPos = spot.clone().subtract(this.schematicCenter).add(entry.getKey());
                    setPos.setBlock(spot.world, entry.getValue()[0], entry.getValue()[1]);
                }
            }
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        if (!init)
        {
            this.init();
        }
        NBTTagCompound blockNBT = nbt.getCompoundTag(BlockList);
        if (this.schematicSize != null)
        {
            nbt.setInteger("sizeX", (int) this.schematicSize.x);
            nbt.setInteger("sizeY", (int) this.schematicSize.y);
            nbt.setInteger("sizeZ", (int) this.schematicSize.z);
        }
        if (this.schematicCenter != null)
        {
            nbt.setInteger("centerX", (int) this.schematicCenter.x);
            nbt.setInteger("centerY", (int) this.schematicCenter.y);
            nbt.setInteger("centerZ", (int) this.schematicCenter.z);
        }
        int i = 0;

        for (Entry<Vector3, int[]> entry : blocks.entrySet())
        {
            String output = "";
            output += entry.getValue()[0];
            output += ":" + entry.getValue()[1];
            output += ":" + ((int) entry.getKey().x) + ":" + ((int) entry.getKey().y) + ":" + ((int) entry.getKey().z);
            blockNBT.setString("Block" + i, output);
            i++;
        }
        blockNBT.setInteger("count", i);
        nbt.setCompoundTag(BlockList, blockNBT);

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        schematicSize = new Vector3(nbt.getInteger("sizeX"), nbt.getInteger("sizeY"), nbt.getInteger("sizeZ"));
        schematicCenter = new Vector3(nbt.getInteger("centerX"), nbt.getInteger("centerY"), nbt.getInteger("centerZ"));
        NBTTagCompound blockDataSave = nbt.getCompoundTag(BlockList);

        for (int blockCount = 0; blockCount < blockDataSave.getInteger("count"); blockCount++)
        {
            String blockString = blockDataSave.getString("Block" + blockCount);
            String[] blockData = blockString.split(":");
            int blockID = 0;
            int blockMeta = 0;
            Vector3 blockPostion = new Vector3();
            if (blockData != null)
            {
                try
                {
                    if (blockData.length > 0)
                    {
                        blockID = Integer.parseInt(blockData[0]);
                    }
                    if (blockData.length > 1)
                    {
                        blockMeta = Integer.parseInt(blockData[1]);
                    }
                    if (blockData.length > 2)
                    {
                        blockPostion.x = Integer.parseInt(blockData[2]);
                    }
                    if (blockData.length > 3)
                    {
                        blockPostion.y = Integer.parseInt(blockData[3]);
                    }
                    if (blockData.length > 4)
                    {
                        blockPostion.z = Integer.parseInt(blockData[4]);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                this.blocks.put(blockPostion, new int[] { blockID, blockMeta });
            }
        }

        if (!init)
        {
            this.init();
        }

    }

    public void getFromResourceFolder(String fileName)
    {
        try
        {
            this.load(CompressedStreamTools.readCompressed(Schematic.class.getResource("/assets/artillects/schematics/" + fileName + ".dat").openStream()));
            this.name = fileName;
            this.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveToBaseDirectory(String fileName)
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            this.save(nbt);
            NBTFileHandler.saveFile(fileName + ".dat", new File(NBTFileHandler.getBaseFolder(), "schematics"), nbt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
