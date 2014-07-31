package artillects.core.creation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ContentBlock extends Content
{
    String unlocalizedName;
    String blockClassName;
    MetaEntry[] subBlocks;

    /** Called to load the content's data from an xml document */
    protected void loadData(Document doc)
    {
        subBlocks = new MetaEntry[16];
        NodeList blockList = doc.getElementsByTagName("block");
        for (int b = 0; b < blockList.getLength(); b++)
        {
            if (blockList.item(b).getNodeType() == Node.ELEMENT_NODE)
            {
                Element block = (Element) blockList.item(b);
                unlocalizedName = block.getAttribute("name");
                NodeList metaList = block.getElementsByTagName("meta");
                for (int m = 0; m < metaList.getLength(); m++)
                {
                    if (metaList.item(m).getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element meta = (Element) metaList.item(m);
                        MetaEntry metaEntry = new MetaEntry();
                        metaEntry.meta = Integer.parseInt(meta.getAttribute("id"));
                        metaEntry.unlocalizedName = meta.getAttribute("name");
                        metaEntry.iconName = meta.getAttribute("icon");                        
                        subBlocks[m] = metaEntry;
                    }
                }
            }
        }
    }

    public void print()
    {
        System.out.println();
        System.out.println("Block: " + unlocalizedName);
        for (int i = 0; i < subBlocks.length; i++)
        {
            if (subBlocks[i] != null)
            {
                System.out.println("\t[" + subBlocks[i].meta + "]Sub: " + subBlocks[i].unlocalizedName);
                System.out.println("\t\tIconName: " + subBlocks[i].iconName);
            }
        }
        System.out.println();
    }

    public static class MetaEntry
    {
        int meta = 0;
        String unlocalizedName;
        String iconName;
    }
}
