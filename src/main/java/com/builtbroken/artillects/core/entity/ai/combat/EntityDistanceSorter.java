package com.builtbroken.artillects.core.entity.ai.combat;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.Entity;

import java.util.Comparator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public class EntityDistanceSorter implements Comparator<Entity>
{
    final IPos3D center;
    final boolean closest;

    public EntityDistanceSorter(IPos3D center)
    {
        this(center, true);
    }

    public EntityDistanceSorter(IPos3D center, boolean closest)
    {
        this.center = center;
        this.closest = closest;
    }

    @Override
    public int compare(Entity o1, Entity o2)
    {
        double d = new Pos(o1).distance(center);
        double d2 = new Pos(o2).distance(center);
        return d > d2 ? 1 : d == d2 ? 0 : -1;
    }
}
