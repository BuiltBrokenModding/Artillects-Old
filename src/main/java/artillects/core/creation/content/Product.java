package artillects.core.creation.content;

import com.builtbroken.mc.core.registry.ModManager;
import org.w3c.dom.Document;

public abstract class Product<N>
{
    protected N product;

    /** Called to load the content's data from an xml document */
    public abstract Product<N> loadData(Document doc);

    /** Creates the object */
    public abstract N create(ModManager creator);
    
    /** Gets the created product */
    public N getProduct()
    {
        return product;
    }
}
