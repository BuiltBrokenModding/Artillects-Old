package artillects.core.creation;

import java.io.IOException;
import java.util.zip.ZipFile;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.ResourceManager;
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
}
