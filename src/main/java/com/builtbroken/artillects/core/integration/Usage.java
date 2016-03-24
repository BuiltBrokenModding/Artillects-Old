package com.builtbroken.artillects.core.integration;

/**
 * NPC understanding of how to use items, tiles, GUI, blocks, etc
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2016.
 */
public abstract class Usage
{
    /** Unique string reference for this usage */
    public final String id;
    /** Localization used for translation */
    public final String localization;

    public Usage(String localization, String id)
    {
        this.localization = localization;
        this.id = id;
    }
}
