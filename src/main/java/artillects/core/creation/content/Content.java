package artillects.core.creation.content;

import org.w3c.dom.Document;

import artillects.core.creation.ContentFactory;
import resonant.lib.content.ContentRegistry;

public abstract class Content
{
    protected ContentFactory loader;

    public Content(ContentFactory loader)
    {
        this.loader = loader;
    }

    /** Called to load the content's data from an xml document */
    public abstract void loadData(Document doc);

    /** Creates the object */
    public abstract void create(ContentRegistry creator);
}
