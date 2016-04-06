package com.builtbroken.artillects.core.entity.ai.npc;

import com.builtbroken.artillects.core.entity.npc.EntityNpc;
import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * Simple task that tells and NPC to move towards his home when he has nothing left to do.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class NpcTaskReturnHome extends NpcTaskStayNearPos
{
    public NpcTaskReturnHome(EntityNpc host)
    {
        super(host, null, 5);
    }

    @Override
    public void updateTask()
    {
        if (host.getAttackTarget() == null && (host.getProfession() == null || host.getProfession().hasActiveTask()))
        {
            super.updateTask();
        }
    }

    @Override
    public IPos3D getPoint()
    {
        return entity().getHomeLocation() != null ? entity().getHomeLocation() : super.getPoint();
    }
}
