package artillects.core.creation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

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

    public static byte[] ZIP_BYTES = { 'P', 'K', 0x3, 0x4 };

    ContentRegistry creator;
    List<Content> loadedContent;

    protected List<DirectTexture> blockTextures;
    protected List<DirectTexture> itemTextures;

    public ContentLoader(ContentRegistry contentRegistry)
    {
        this.creator = contentRegistry;
        loadedContent = new LinkedList<Content>();
        blockTextures = new LinkedList<DirectTexture>();
        itemTextures = new LinkedList<DirectTexture>();
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
        ContentBlock content = null;
        String name = file.getName();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if (file.getName().endsWith(".png"))
        {
            if (file.getName().startsWith("block."))
            {
                blockTextures.add(new DirectTexture(file.getName().replace(".png", "").replace("block.", ""), file));
            }
            else if (file.getName().startsWith("item."))
            {
                itemTextures.add(new DirectTexture(file.getName().replace(".png", "").replace("item.", ""), file));
            }
        }
        else if (name.endsWith(".xml"))
        {
            InputStream stream = new FileInputStream(file);
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            if (doc.getElementsByTagName("block").getLength() > 0)
            {
                content = new ContentBlock(this);
            }
            else if (doc.getElementsByTagName("item").getLength() > 0)
            {

            }
            else if (doc.getElementsByTagName("tile").getLength() > 0)
            {

            }
            else if (doc.getElementsByTagName("entity").getLength() > 0)
            {

            }
            else
            {
                return;
            }
            content.loadData(doc);
            stream.close();
            loadedContent.add(content);
            return;
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
                        content = new ContentBlock(this);
                    }
                    else if (doc.getElementsByTagName("item").getLength() > 0)
                    {

                    }
                    else if (doc.getElementsByTagName("tile").getLength() > 0)
                    {

                    }
                    else if (doc.getElementsByTagName("entity").getLength() > 0)
                    {

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
                    if (entry.getName().startsWith("block."))
                    {
                        blockTextures.add(new DirectTexture(entry.getName().replace(".png", "").replace("block.", ""), zipFile));
                    }
                    else if (entry.getName().startsWith("item."))
                    {
                        itemTextures.add(new DirectTexture(entry.getName().replace(".png", "").replace("item.", ""), zipFile));
                    }
                }
            }
            loadedContent.add(content);
        }
    }

    public void createAll()
    {
        for (Content content : this.loadedContent)
        {
            create(content);
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
            for (DirectTexture texture : blockTextures)
            {
                event.map.setTextureEntry(texture.getIconName(), texture);
            }
        }
        else if (event.map.textureType == 1)
        {
            for (DirectTexture texture : itemTextures)
            {
                event.map.setTextureEntry(texture.getIconName(), texture);
            }
        }
    }

    /** The method to test if a input stream is a zip archive.
     * 
     * @param in the input stream to test.
     * @return */
    public static boolean isZipStream(InputStream in)
    {
        if (!in.markSupported())
        {
            in = new BufferedInputStream(in);
        }
        boolean isZip = true;
        try
        {
            in.mark(ZIP_BYTES.length);
            for (int i = 0; i < ZIP_BYTES.length; i++)
            {
                if (ZIP_BYTES[i] != (byte) in.read())
                {
                    isZip = false;
                    break;
                }
            }
            in.reset();
        }
        catch (IOException e)
        {
            isZip = false;
        }
        return isZip;
    }

    /** Test if a file is a zip file.
     * 
     * @param f the file to test.
     * @return */
    public static boolean isZipFile(File f)
    {

        boolean isZip = true;
        byte[] buffer = new byte[ZIP_BYTES.length];
        try
        {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            raf.readFully(buffer);
            for (int i = 0; i < ZIP_BYTES.length; i++)
            {
                if (buffer[i] != ZIP_BYTES[i])
                {
                    isZip = false;
                    break;
                }
            }
            raf.close();
        }
        catch (Throwable e)
        {
            isZip = false;
        }
        return isZip;
    }
}
