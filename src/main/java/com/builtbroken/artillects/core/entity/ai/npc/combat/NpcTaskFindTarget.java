package com.builtbroken.artillects.core.entity.ai.npc.combat;

import com.builtbroken.artillects.core.entity.ai.combat.AITaskFindTarget;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionCombat;
import net.minecraft.command.IEntitySelector;

/**
 * Modified version of {@link AITaskFindTarget} that is more refined towards NPCs then generic AIs
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class NpcTaskFindTarget extends AITaskFindTarget<EntityNpc>
{
    public NpcTaskFindTarget(EntityNpc host, IEntitySelector selector)
    {
        super(host, selector);
    }

    @Override
    public void updateTask()
    {
        //TODO add priotization for some entities over others. Eg kill creepers, then skeletons, then zombies, then spiders. However, if zombie is close enough kill first. In other words need a weight system, that also keeps in mind other NPCs.
        //TODO keep track of which NPC is attack which entity. This way damage can be spread out evenly so not to waste resources on one target. Eg two swordsman to one zombie, one archer to one zombie, 3 arches to one creeper.
        if (host.profession instanceof ProfessionCombat && ((ProfessionCombat) host.profession).findTargets)
        {
            super.updateTask();
        }
    }
}
