package artillects.core.creation;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
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

    public Content()
    {

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
    protected void load(File file) throws Exception
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        loadData(doc);
    }

    /** Called to load the content's data from an xml document */
    protected void loadData(Document doc)
    {

    }
}
