package artillects.hive.schematics;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.tuple.Pair;

import artillects.Vector3;
import artillects.hive.ISaveObject;

/** File that represents all the data loaded from a schematic data file
 * 
 * @author Dark */
public class Schematic implements ISaveObject
{
    public static final String BlockList = "BlockList";

    protected File file;
    protected String name;
    protected Vector3 schematicSize, schematicCenter;

    protected HashMap<Vector3, int[]> blocks = new HashMap<Vector3, int[]>();

    public Schematic(File file)
    {
        this.file = file;
    }

    public String getFileName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        // TODO Auto-generated method stub

    }

    public void getFromResourceFolder(String fileName)
    {
        try
        {
            //Open file
            URL url = Schematic.class.getResource("/assets/artillects/schematics/" + fileName + ".dat");
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(url.openStream());
            //Read base data
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }
}
