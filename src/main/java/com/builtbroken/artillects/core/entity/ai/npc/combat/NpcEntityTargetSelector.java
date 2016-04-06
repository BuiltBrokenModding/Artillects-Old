package com.builtbroken.artillects.core.entity.ai.npc.combat;

import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionGuard;
import com.builtbroken.mc.prefab.entity.selector.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class NpcEntityTargetSelector extends EntitySelector
{
    private EntityNpc host;

    public NpcEntityTargetSelector(EntityNpc npc)
    {
        this.host = npc;
    }

    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        //TODO add check if NPC can engage the target safely. No weapons == not safe, wood sword == risky, stone sword == not smart, iron sword == we can do this
        if (entity.isEntityAlive() && !entity.isEntityInvulnerable())
        {
            //TODO add checks if entity can be ignored, no need to kill everything. Eg baby slimes are not a threat to anything, just annoying.
            //TODO add mod support
            if(insideKillArea(entity))
            {
                //TODO add handling for flying or floating units so melee units don't chase
                if (entity instanceof IMob)
                {
                    return true;
                }
            }
        }
        return false;
    }

    //Segements to make things easier to read, handles distance checks to ensure NPC is not leaving his post
    private final boolean insideKillArea(Entity entity)
    {
        if(host.profession instanceof ProfessionGuard && ((ProfessionGuard) host.profession).stayNearZone)
        {
            //Targets outside of guard zones need to be ignores
            //TODO add check if entity enters a protection zone, override distance check if true
            //TODO add checks if entity is about to danger another NPC, override distance check if true
            //TODO ensure that the zone we are protecting is not left unprotected if we engage NPC, in other prioritization system is needed (setting to for player to adjust [always stay, maintain some protection, who cares])
            if(((ProfessionGuard) host.profession).centerOfGuardZone != null)
            {
                double distance = ((ProfessionGuard) host.profession).centerOfGuardZone.distance(entity);
                //TODO add motion check to see if entity is leaving the area, ignore entities that are going to be outside the zone. Especially if they leave by the time the NPC reaches them.
                if(distance > ((ProfessionGuard) host.profession).zoneDistance - 1)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
