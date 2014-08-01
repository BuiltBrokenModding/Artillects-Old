package artillects.core.creation.content;

import li.cil.oc.api.driver.Item;

import org.w3c.dom.Document;

import artillects.core.creation.ContentFactory;
import resonant.lib.content.ContentRegistry;

public class ItemProduct extends Product<Item>
{
    public Item item;
    public String unlocalizedName;
    
    public ItemProduct(ContentFactory loader)
    {
        super(loader);
    }

    @Override
    public ItemProduct loadData(Document doc)
    {
        return this;
    }

    @Override
    public Item create(ContentRegistry creator)
    {
        return product;
    }

}
