package artillects.core.creation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.data.MetadataSection;


public class FileResource implements Resource
{
    File file;
    InputStream stream;
    String name;

    public FileResource(String name, File file)
    {
        this.name = name;
        this.file = file;
    }

    @Override
    public InputStream getInputStream()
    {
        if (stream == null)
        {
            try
            {
                stream = new FileInputStream(file);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return stream;
    }

    @Override
    public boolean hasMetadata()
    {
        return false;
    }

    @Override
    public MetadataSection getMetadata(String s)
    {
        return null;
    }
}