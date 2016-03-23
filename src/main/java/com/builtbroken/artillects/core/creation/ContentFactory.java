package com.builtbroken.artillects.core.creation;

import com.builtbroken.artillects.core.creation.content.BlockProduct;
import com.builtbroken.artillects.core.creation.content.ItemProduct;
import com.builtbroken.artillects.core.creation.content.Product;
import com.builtbroken.artillects.core.creation.content.ProductType;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.world.schematic.SchematicMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Loads data from xml files to be used in creating content objects such as blocks
 * 
 * @author Darkguardsman */
public class ContentFactory
{
    private ModManager creator;

    public HashMap<Class<?>, HashMap<String, Product>> content = new HashMap<Class<?>, HashMap<String, Product>>();

    public ContentFactory(ModManager contentRegistry)
    {
        this.creator = contentRegistry;
    }

    public void load() throws Exception
    {
        URL url = SchematicMap.class.getResource("/assets/artillects/content/");
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
        Product product = null;
        ProductType type = null;
        String name = file.getName();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if (file.getName().endsWith(".png"))
        {
            String textureName = file.getName().replace(".png", "");
            if (file.getName().startsWith("block."))
            {
                textureName = textureName.replace("block.", "");
                //blockTextures.put(textureName, new DirectTexture(textureName, file));
            }
            else if (file.getName().startsWith("item."))
            {
                textureName = textureName.replace("item.", "");
                //itemTextures.put(textureName, new DirectTexture(textureName, file));
            }
        }
        else if (name.endsWith(".xml"))
        {
            InputStream stream = new FileInputStream(file);
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            if (doc.getElementsByTagName("block").getLength() > 0)
            {
                type = ProductType.BLOCK;
                product = new BlockProduct();
            }
            else if (doc.getElementsByTagName("item").getLength() > 0)
            {
                type = ProductType.ITEM;
                product = new ItemProduct();
            }
            else
            {
                return;
            }
            product.loadData(doc);
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
                        type = ProductType.BLOCK;
                        product = new BlockProduct();
                    }
                    else if (doc.getElementsByTagName("item").getLength() > 0)
                    {
                        type = ProductType.ITEM;
                    }
                    else
                    {
                        continue;
                    }
                    product.loadData(doc);
                    stream.close();
                }
                else if (entry.getName().endsWith(".png"))
                {
                    String textureName = entry.getName().replace(".png", "");
                    if (file.getName().startsWith("block."))
                    {
                        textureName = textureName.replace("block.", "");
                        //blockTextures.put(textureName, new DirectTexture(textureName, file));
                    }
                    else if (file.getName().startsWith("item."))
                    {
                        textureName = textureName.replace("item.", "");
                        //itemTextures.put(textureName, new DirectTexture(textureName, file));
                    }
                }
            }
        }
        if (product != null)
        {
            if (type == ProductType.BLOCK)
            {
                HashMap<String, Product> c = content.containsKey(Block.class) ? content.get(Block.class) : new HashMap<String, Product>();
                c.put(((BlockProduct) product).unlocalizedName, product);
                content.put(Block.class, c);
            }
            else if (type == ProductType.ITEM)
            {
                HashMap<String, Product> c = content.containsKey(Item.class) ? content.get(Item.class) : new HashMap<String, Product>();
                c.put(((ItemProduct) product).unlocalizedName, product);
                content.put(Item.class, c);
            }
        }
    }

    public void createAll()
    {
        for (Entry<Class<?>, HashMap<String, Product>> entry : content.entrySet())
        {
            if(entry.getValue() != null)
            {
                for(Entry<String, Product> e : entry.getValue().entrySet())
                {
                    if(e.getValue() != null)
                    {
                        create(e.getValue());
                    }
                }
            }
        }
    }

    public void create(Product product)
    {
        product.create(creator);
    }

}
