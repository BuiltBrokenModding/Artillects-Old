package com.builtbroken.artillects.core.creation.content;

import com.builtbroken.mc.core.registry.ModManager;
import net.minecraft.item.Item;
import org.w3c.dom.Document;

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
