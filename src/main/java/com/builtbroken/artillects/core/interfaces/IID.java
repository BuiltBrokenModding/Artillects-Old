package com.builtbroken.artillects.core.interfaces;

/** Used for objects that can be identified by a string */
@Deprecated //Needs to go away as this will cause problems with implementing two interfaces sharing the same system with different ID types
public interface IID<I, C extends Object>
{
    /** String id */
    I getID();

    /** Sets the id for the land */
    C setID(I id);
}
