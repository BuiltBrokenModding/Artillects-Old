package artillects.core.creation;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.ResourceLocation;

public class DirectTexture extends TextureAtlasSprite
{
    ZipFile zipFile;
    File file;

    protected DirectTexture(String name, ZipFile zipFile)
    {
        super(name);
        this.zipFile = zipFile;
    }

    protected DirectTexture(String name, File file)
    {
        super(name);
        this.file = file;
    }

    @Override
    public boolean load(ResourceManager manager, ResourceLocation location) throws IOException
    {
        Resource resource = null;
        if (zipFile != null)
        {
            resource = new ZipResource(this.getIconName(), zipFile);
        }
        else if (file != null)
        {
            resource = new FileResource(this.getIconName(), file);
        }
        loadSprite(resource);
        resource.getInputStream().close();
        return true;
    }
}
