package com.builtbroken.artillects.core.entity.profession.combat;

import com.builtbroken.artillects.core.entity.ai.npc.combat.NpcEntityTargetSelector;
import com.builtbroken.artillects.core.entity.ai.npc.combat.NpcTaskFindTarget;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.Profession;

/**
 * NPC profession geared towards combat
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public abstract class ProfessionCombat extends Profession<EntityNpc>
{
    /** Are we searching for targets */
    public boolean findTargets = true;

    public ProfessionCombat(EntityNpc entity)
    {
        super(entity);
    }

    @Override
    public void loadAITasks()
    {
        entity().tasks.add(2, new NpcTaskFindTarget(entity(), new NpcEntityTargetSelector(entity())));
    }

    @Override
    public void unloadAITask()
    {
        //TODO implement
    }
}
