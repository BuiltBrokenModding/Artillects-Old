package artillects.core.creation;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipFile;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import resonant.lib.content.ContentRegistry;
import resonant.lib.render.RenderUtility;
import artillects.core.building.BuildFile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Loads data from xml files to be used in creating content objects such as blocks
 * 
 * @author Darkguardsman */
public class ContentLoader
{
    ContentRegistry creator;
    List<Content> loadedContent;

    public ContentLoader(ContentRegistry contentRegistry)
    {
        this.creator = contentRegistry;
        loadedContent = new LinkedList<Content>();
    }

    public void load() throws Exception
    {
        URL url = BuildFile.class.getResource("/assets/artillects/content/");
        if (url == null)
        {
            throw new RuntimeException("[Artillects] failed to load content folder");
        }
        else
        {
            loadAll(new File(url.toURI()));
        }
    }

    public void loadAll(File folder) throws Exception
    {
        for (final File file : folder.listFiles())
        {
            if (file.isDirectory())
                loadAll(file);
            else
                load(file);
        }
    }

    public void load(File file) throws Exception
    {
        ContentBlock content = null;
        if (file.getName().endsWith(".block"))
        {
            content = new ContentBlock();
        }
        else if (file.getName().endsWith(".tile"))
        {
            //loadTile(file);
        }
        else if (file.getName().endsWith(".entity"))
        {
            //loadEntity(file);
        }
        else if (file.getName().endsWith(".item"))
        {
            //loadItem(file);
        }
        if (content != null)
        {
            content.load(new ZipFile(file));
            loadedContent.add(content);
        }
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent.Pre event)
    {

        for (Content content : loadedContent)
        {
            if (event.map.textureType == 0)
            {
                for (ZipTexture texture : content.blockTextures)
                {
                    event.map.setTextureEntry(texture.getIconName(), texture);
                }
            }
            else if (event.map.textureType == 1)
            {
                for (ZipTexture texture : content.itemTextures)
                {
                    event.map.setTextureEntry(texture.getIconName(), texture);
                }
            }
        }
    }
}
