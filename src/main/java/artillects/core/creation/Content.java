package artillects.core.creation;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Content
{
    private String name;
    protected List<ZipTexture> blockTextures;
    protected List<ZipTexture> itemTextures;

    public Content()
    {
        blockTextures = new LinkedList<ZipTexture>();
        itemTextures = new LinkedList<ZipTexture>();
    }

    public Content(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /** Called to load the file's content
     * 
     * @throws SAXException
     * @throws ParserConfigurationException */
    protected void load(ZipFile file) throws Exception
    {
        Enumeration<? extends ZipEntry> entries = file.entries();

        while (entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equalsIgnoreCase("content.xml"))
            {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputStream stream = file.getInputStream(entry);
                Document doc = dBuilder.parse(stream);
                doc.getDocumentElement().normalize();
                loadData(doc);
                stream.close();
            }
            else if (entry.getName().endsWith(".png"))
            {
                if (entry.getName().startsWith("block."))
                {
                    blockTextures.add(new ZipTexture(entry.getName().replace(".png", "").replace("block.", ""), file));
                }
                else if (entry.getName().startsWith("item."))
                {
                    itemTextures.add(new ZipTexture(entry.getName().replace(".png", "").replace("item.", ""), file));
                }
            }
        }
    }

    /** Called to load the content's data from an xml document */
    protected void loadData(Document doc)
    {

    }
}
