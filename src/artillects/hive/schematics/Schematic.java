package artillects.hive.schematics;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import artillects.Vector3;
import artillects.hive.ISaveObject;

/** File that represents all the data loaded from a schematic data file
 * 
 * @author Dark */
public class Schematic implements ISaveObject
{
    public static final String BlockList = "BlockList";

    protected String name;
    protected Vector3 schematicSize, schematicCenter;

    protected HashMap<Vector3, int[]> blocks = new HashMap<Vector3, int[]>();

    public String getFileName()
    {
        return this.name;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        NBTTagCompound blockNBT = nbt.getCompoundTag(BlockList);
        nbt.setInteger("sizeX", (int) this.schematicSize.x);
        nbt.setInteger("sizeY", (int) this.schematicSize.y);
        nbt.setInteger("sizeZ", (int) this.schematicSize.z);
        nbt.setInteger("centerX", (int) this.schematicCenter.x);
        nbt.setInteger("centerY", (int) this.schematicCenter.y);
        nbt.setInteger("centerZ", (int) this.schematicCenter.z);
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

    }

    public void getFromResourceFolder(String fileName)
    {
        try
        {
            this.load(CompressedStreamTools.readCompressed(Schematic.class.getResource("/assets/artillects/schematics/" + fileName + ".dat").openStream()));
            this.name = fileName;
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
