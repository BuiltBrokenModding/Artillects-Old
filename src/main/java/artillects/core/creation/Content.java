package artillects.core.creation;

import org.w3c.dom.Document;

import resonant.lib.content.ContentRegistry;

public abstract class Content
{
    protected ContentLoader loader;

    public Content(ContentLoader loader)
    {
        this.loader = loader;
    }

    /** Called to load the content's data from an xml document */
    protected abstract void loadData(Document doc);

    /** Creates the object */
    public abstract void create(ContentRegistry creator);
}
