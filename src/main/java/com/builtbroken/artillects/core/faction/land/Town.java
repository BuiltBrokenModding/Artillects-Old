package com.builtbroken.artillects.core.faction.land;

/**
 * Generalized name for any collection of people resulting in a permanent series of structures
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/25/2016.
 */
public class Town extends Region
{
    //TODO implement town levels
    //TODO implement region control creep
    //TODO implement town value(point value of all items and builds, used for raiding by bandits... etc)
    //TODO implement power level(Military force + Political power + Cultural power)
    //TODO implement region control overtake if neighbor region power is too low, only if enemy region & not inside minimal control area
    //TODO implement min and max land control area
    //TODO implement tech limits based on town size
    //TODO implement population limits based on town size, with peak max(eg NPCs that join the town not spawned by the town)
    //TODO implement town center NPC spawning
    //TODO implement town center levels(Adds to town level but is not the only contributor to level)
    //TODO enforce at least one path to town center(Used by NPC spawning, and also prevents ignoring bandits)

    protected Town(String name)
    {
        super(name);
    }
}
