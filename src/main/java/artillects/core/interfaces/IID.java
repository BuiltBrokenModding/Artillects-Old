package artillects.core.interfaces;

/** Used for objects that can be identified by a string */
public interface IID<I, C extends Object>
{
    /** String id */
    I getID();

    /** Sets the id for the land */
    C setID(I id);
}
