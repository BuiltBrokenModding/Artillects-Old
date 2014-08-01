package artillects.core.creation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import resonant.lib.content.ContentRegistry;
import resonant.lib.prefab.item.ItemBlockMetadata;
import artillects.core.creation.templates.BlockTemplate;

public class ContentBlock extends Content
{
    String unlocalizedName;
    Material material = Material.circuits;
    Subblock[] subBlocks;

    public ContentBlock(ContentLoader loader)
    {
        super(loader);
    }

    /** Called to load the content's data from an xml document */
    protected void loadData(Document doc)
    {
        subBlocks = new Subblock[16];
        NodeList blockList = doc.getElementsByTagName("block");
        for (int b = 0; b < blockList.getLength(); b++)
        {
            if (blockList.item(b).getNodeType() == Node.ELEMENT_NODE)
            {
                Element block = (Element) blockList.item(b);
                if (block.hasAttribute("name"))
                {
                    unlocalizedName = block.getAttribute("name");
                    NodeList metaList = block.getElementsByTagName("meta");
                    for (int m = 0; m < metaList.getLength(); m++)
                    {
                        if (metaList.item(m).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element meta = (Element) metaList.item(m);
                            if (meta.hasAttribute("id"))
                            {
                                Subblock subblock = new Subblock();
                                subblock.meta = Integer.parseInt(meta.getAttribute("id"));
                                subblock.unlocalizedName = meta.hasAttribute("name") ? meta.getAttribute("name") : unlocalizedName;
                                subblock.iconName = meta.hasAttribute("icon") ? meta.getAttribute("icon") : null;
                                subBlocks[m] = subblock;
                            }
                            else
                            {
                                throw new IllegalArgumentException("Block " + unlocalizedName + "'s subblock meta must have a meta id");
                            }
                        }
                    }
                }
                else
                {
                    throw new IllegalArgumentException("BlockFile " + doc.getDocumentURI() + " doesn't contain a name attribute");
                }
            }
        }
    }

    @Override
    public void create(ContentRegistry creator)
    {
        try
        {
            int assignedID = creator.idManager.getNextBlockID();
            int actualID = creator.config.getBlock(unlocalizedName, assignedID).getInt(assignedID);
            Block block = new BlockTemplate(actualID, material);
            ContentRegistry.proxy.registerBlock(creator, block, ItemBlockMetadata.class, unlocalizedName, creator.modID);
            creator.finishCreation(block, null);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
