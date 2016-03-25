package com.builtbroken.artillects.core.faction;

import com.builtbroken.mc.lib.access.Permission;

/**
 * Static class used to store all references to permission nodes used for faction features
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public final class FactionPerms
{
    /** Base root of all faction perms */
    public static Permission FACTION_PERM = new Permission("faction");



    /** Base root of all claim nodes */
    public static Permission FACTION_CLAIM = FACTION_PERM.addChild("claim");
    /** Can a user claim a chunk for the faction */
    public static Permission FACTION_CLAIM_CHUNK = FACTION_CLAIM.addChild("chunk");
    /** Can a user claim a region({@link com.builtbroken.artillects.core.faction.land.Land}) for the faction */
    public static Permission FACTION_CLAIM_REGION = FACTION_CLAIM.addChild("region");
    /** Can a user claim a {@link com.builtbroken.artillects.core.faction.land.Town} for the faction */
    public static Permission FACTION_CLAIM_TOWN = FACTION_CLAIM.addChild("town");



    /** Base root of all new objects created by the faction */
    public static Permission FACTION_NEW = FACTION_PERM.addChild("new");
    /** Can a user create a new {@link com.builtbroken.artillects.core.faction.land.Town} */
    public static Permission FACTION_NEW_TOWN = FACTION_NEW.addChild("new");
    /** Can a user create a new fort */
    public static Permission FACTION_NEW_FORT = FACTION_NEW.addChild("fort");
    /** Can a user create a new camp */
    public static Permission FACTION_NEW_CAMP = FACTION_NEW.addChild("camp");
}
