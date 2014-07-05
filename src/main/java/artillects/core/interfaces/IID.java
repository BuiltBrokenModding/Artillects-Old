package artillects.core.interfaces;

import java.util.UUID;

import artillects.core.region.Land;

/** Used for objects that can be identified by a string */
public interface IID<I, C extends Object>
{
    /** String id */
    public I getID();

    /** Sets the id for the land */
    public C setID(I id);
}
