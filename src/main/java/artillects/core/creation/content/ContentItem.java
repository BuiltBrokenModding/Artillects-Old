package artillects.core.creation.content;

import li.cil.oc.api.driver.Item;

import org.w3c.dom.Document;

import artillects.core.creation.ContentLoader;
import resonant.lib.content.ContentRegistry;

public class ContentItem extends Content
{
    public Item item;
    public String unlocalizedName;
    
    public ContentItem(ContentLoader loader)
    {
        super(loader);
    }

    @Override
    public void loadData(Document doc)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void create(ContentRegistry creator)
    {
        // TODO Auto-generated method stub

    }

}
