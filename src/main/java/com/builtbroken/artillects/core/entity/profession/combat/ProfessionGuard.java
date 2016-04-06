package com.builtbroken.artillects.core.entity.profession.combat;

import com.builtbroken.artillects.core.entity.ai.npc.combat.NpcTaskGuardArea;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.mc.lib.transform.vector.Pos;

/**
 * NPC profession geared towards protected areas, not just fighting.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public class ProfessionGuard extends ProfessionCombat
{
    /** Should we stay near our protection zone */
    public boolean stayNearZone = true;
    public double zoneDistance = 50;

    /** TEMP var will be replaced with guard task, used to track center of guard position */
    public Pos centerOfGuardZone;

    public ProfessionGuard(EntityNpc entity)
    {
        super(entity);
        entity.tasks.add(2, new NpcTaskGuardArea(entity, zoneDistance)); //TODO add getter so we can update the distance easier
    }

    @Override
    public void loadAITasks()
    {
        super.loadAITasks();
    }

    @Override
    public void unloadAITask()
    {
        super.unloadAITask();
    }
}
