package artillects.hive.schematics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

/** Uses to handle loading and saving of NBT files
 * 
 * @author Dark */
public class NBTFileHandler
{
    public static boolean saveFile(String filename, File directory, NBTTagCompound data)
    {
        try
        {
            File tempFile = new File(directory, filename + "_tmp");
            File file = new File(directory, filename);
            file.mkdirs();
            CompressedStreamTools.writeCompressed(data, new FileOutputStream(tempFile));

            if (file.exists())
            {
                file.delete();
            }

            tempFile.renameTo(file);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static NBTTagCompound loadFile(File saveDirectory, String filename)
    {
        return loadFile(new File(saveDirectory, filename));
    }

    public static NBTTagCompound loadFile(File file)
    {
        try
        {
            if (file.exists())
            {
                return CompressedStreamTools.readCompressed(new FileInputStream(file));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    public static File getWorldSaveFolder(String worldName)
    {
        File parent = getBaseFolder();

        if (FMLCommonHandler.instance().getSide().isClient())
        {
            parent = new File(getBaseFolder(), "saves" + File.separator);
        }

        return new File(parent, worldName + File.separator);
    }

    public static File getBaseFolder()
    {
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            FMLClientHandler.instance().getClient();
            return Minecraft.getMinecraft().mcDataDir;
        }
        else
        {
            return new File(".");
        }
    }

}
