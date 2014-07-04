package artillects.core.interfaces;

import java.util.UUID;

import artillects.core.region.Land;

/** Used for objects that can be identified by a string */
public interface IID
{
    /** String id */
    public UUID getID();

    /** Sets the id for the land */
    public Land setID(UUID id);
}
