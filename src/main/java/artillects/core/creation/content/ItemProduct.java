package artillects.core.creation.content;

import net.minecraft.item.Item;
import org.w3c.dom.Document;
import resonant.lib.mod.content.ModManager;

public class ItemProduct extends Product<Item>
{
    public String unlocalizedName;

    @Override
    public ItemProduct loadData(Document doc)
    {
        return this;
    }

    @Override
    public Item create(ModManager creator)
    {
        return product;
    }

}
