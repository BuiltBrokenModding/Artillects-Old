package artillects.core.creation.content;

import net.minecraft.item.Item;

import org.w3c.dom.Document;

import artillects.core.creation.ContentFactory;
import resonant.lib.content.ContentRegistry;

public class ItemProduct extends Product<Item>
{
    public String unlocalizedName;

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
