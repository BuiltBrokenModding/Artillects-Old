package artillects.content.blocks.door;

import artillects.core.creation.content.Product;
import org.w3c.dom.Document;
import com.builtbroken.lib.mod.content.ModManager;

public class DoorProduct extends Product<DoorProduct>
{
    @Override
    public Product<DoorProduct> loadData(Document doc)
    {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public DoorProduct create(ModManager creator)
    {        
        return this;
    }

}
