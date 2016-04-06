package com.builtbroken.artillects.core.entity.ai.npc.combat;

import com.builtbroken.artillects.core.entity.ai.npc.NpcTaskStayNearPos;
import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionGuard;
import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class NpcTaskGuardArea extends NpcTaskStayNearPos
{
    public NpcTaskGuardArea(EntityNpc host, double distance)
    {
        super(host, null, distance);
    }

    @Override
    public IPos3D getPoint()
    {
        return host.getProfession() instanceof ProfessionGuard ? ((ProfessionGuard) host.getProfession()).centerOfGuardZone : super.getPoint();
    }
}
