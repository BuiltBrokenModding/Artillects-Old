package artillects.core.creation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.data.MetadataSection;
import net.minecraft.util.ResourceLocation;

public class ZipTexture extends TextureAtlasSprite
{
    ZipFile zipFile;

    protected ZipTexture(String name, ZipFile zipFile)
    {
        super(name);
        this.zipFile = zipFile;
    }

    @Override
    public boolean load(ResourceManager manager, ResourceLocation location) throws IOException
    {
        ZipResource resource = new ZipResource(this.getIconName(), zipFile);
        loadSprite(resource);
        resource.getInputStream().close();
        return true;
    }

    public static class ZipResource implements Resource
    {
        ZipFile zipFile;
        InputStream stream;
        String name;

        public ZipResource(String name, ZipFile file)
        {
            this.name = name;
            this.zipFile = file;
        }

        @Override
        public InputStream getInputStream()
        {
            if (stream == null)
            {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().equalsIgnoreCase(name))
                    {
                        try
                        {
                            stream = zipFile.getInputStream(entry);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
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
}
