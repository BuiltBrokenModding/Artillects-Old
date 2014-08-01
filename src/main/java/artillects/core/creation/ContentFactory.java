package artillects.core.creation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;

import org.w3c.dom.Document;

import resonant.lib.content.ContentRegistry;
import artillects.core.building.BuildFile;
import artillects.core.creation.content.Content;
import artillects.core.creation.content.ContentBlock;
import artillects.core.creation.content.ContentItem;
import artillects.core.creation.content.ContentType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Loads data from xml files to be used in creating content objects such as blocks
 * 
 * @author Darkguardsman */
public class ContentFactory
{
    private ContentRegistry creator;
    List<Content> loadedContent;

    public static HashMap<String, DirectTexture> blockTextures = new HashMap<String, DirectTexture>();
    public static HashMap<String, DirectTexture> itemTextures = new HashMap<String, DirectTexture>();

    public static HashMap<String, ContentBlock> blocks = new HashMap<String, ContentBlock>();
    public static HashMap<String, ContentItem> items = new HashMap<String, ContentItem>();

    public ContentFactory(ContentRegistry contentRegistry)
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
            {
                loadAll(file);
            }
            else
            {
                load(file);
            }
        }
    }

    public void load(File file) throws Exception
    {
        Content content = null;
        ContentType type = null;
        String name = file.getName();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if (file.getName().endsWith(".png"))
        {
            String textureName = file.getName().replace(".png", "");
            if (file.getName().startsWith("block."))
            {
                textureName = textureName.replace("block.", "");
                blockTextures.put(textureName, new DirectTexture(textureName, file));
            }
            else if (file.getName().startsWith("item."))
            {
                textureName = textureName.replace("item.", "");
                itemTextures.put(textureName, new DirectTexture(textureName, file));
            }
        }
        else if (name.endsWith(".xml"))
        {
            InputStream stream = new FileInputStream(file);
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            if (doc.getElementsByTagName("block").getLength() > 0)
            {
                type = ContentType.BLOCK;
                content = new ContentBlock(this);
            }
            else if (doc.getElementsByTagName("item").getLength() > 0)
            {
                type = ContentType.ITEM;
            }
            else if (doc.getElementsByTagName("tile").getLength() > 0)
            {
                type = ContentType.TILE;
            }
            else if (doc.getElementsByTagName("entity").getLength() > 0)
            {
                type = ContentType.ENTITY;
            }
            else
            {
                return;
            }
            content.loadData(doc);
            stream.close();
        }
        else if (name.endsWith(".zip"))
        {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".xml"))
                {
                    InputStream stream = zipFile.getInputStream(entry);
                    Document doc = dBuilder.parse(stream);
                    doc.getDocumentElement().normalize();
                    if (doc.getElementsByTagName("block").getLength() > 0)
                    {
                        type = ContentType.BLOCK;
                        content = new ContentBlock(this);
                    }
                    else if (doc.getElementsByTagName("item").getLength() > 0)
                    {
                        type = ContentType.ITEM;
                    }
                    else if (doc.getElementsByTagName("tile").getLength() > 0)
                    {
                        type = ContentType.TILE;
                    }
                    else if (doc.getElementsByTagName("entity").getLength() > 0)
                    {
                        type = ContentType.ENTITY;
                    }
                    else
                    {
                        continue;
                    }
                    content.loadData(doc);
                    stream.close();
                }
                else if (entry.getName().endsWith(".png"))
                {
                    String textureName = entry.getName().replace(".png", "");
                    if (file.getName().startsWith("block."))
                    {
                        textureName = textureName.replace("block.", "");
                        blockTextures.put(textureName, new DirectTexture(textureName, file));
                    }
                    else if (file.getName().startsWith("item."))
                    {
                        textureName = textureName.replace("item.", "");
                        itemTextures.put(textureName, new DirectTexture(textureName, file));
                    }
                }
            }
        }
        if (content != null)
        {
            if (type == ContentType.BLOCK)
            {
                blocks.put(((ContentBlock) content).unlocalizedName, (ContentBlock) content);
            }
            else if (type == ContentType.ITEM)
            {
                items.put(((ContentItem) content).unlocalizedName, (ContentItem) content);
            }
            else
            {
                loadedContent.add(content);
            }
        }
    }

    public void createAll()
    {
        for (Content content : this.loadedContent)
        {
            create(content);
        }
        for(ContentBlock block : blocks.values())
        {
            block.create(creator);
        }        
        for(ContentItem item : items.values())
        {
            item.create(creator);
        }
    }

    public void create(Content content)
    {
        content.create(creator);
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent.Pre event)
    {
        if (event.map.textureType == 0)
        {
            for (DirectTexture texture : blockTextures.values())
            {
                event.map.setTextureEntry(texture.getIconName(), texture);
            }
        }
        else if (event.map.textureType == 1)
        {
            for (DirectTexture texture : itemTextures.values())
            {
                event.map.setTextureEntry(texture.getIconName(), texture);
            }
        }
    }
}
