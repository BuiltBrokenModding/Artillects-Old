package com.builtbroken.artillects.core.entity.ai.npc;

import com.builtbroken.artillects.core.entity.ai.AITask;
import com.builtbroken.artillects.core.entity.ai.PositionGenerator;
import com.builtbroken.artillects.core.entity.npc.EntityNpc;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.imp.transform.vector.Pos;

/**
 * Used by NPCs to stay in a select area. Similar to {@link net.minecraft.entity.ai.EntityAIMoveTowardsRestriction} with more delay. Allowing the
 * NPC to continue to move outside the zone as needed.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class NpcTaskStayNearPos extends AITask<EntityNpc>
{
    static final int MOVE_DELAY = 20 * 60;

    private IPos3D point;
    public double minDistance;

    private int updateDelay = 0;
    private int forceMoveDelay = MOVE_DELAY;

    public NpcTaskStayNearPos(EntityNpc host, IPos3D point, double distance)
    {
        super(host);
        this.setPoint(point);
        this.minDistance = distance;
    }

    @Override
    public void updateTask()
    {
        if (--updateDelay <= 0)
        {
            updateDelay = 5;
            if (getPoint() == null)
            {
                setPoint(entity().getHomeLocation());
            }

            if (getPoint() != null)
            {
                if (host.getDistance(getPoint().x(), getPoint().y(), getPoint().z()) > minDistance)
                {
                    if (--forceMoveDelay <= 0)
                    {
                        forceMoveDelay = 5;
                        moveTowardsZone();
                    }
                }
                else
                {
                    forceMoveDelay = MOVE_DELAY;
                }
            }
        }
    }

    /**
     * Called to force the NPC to move towards the point
     */
    protected void moveTowardsZone()
    {
        //TODO check pathfinder to see if we are already moving towards home
        //TODO ensure the NPC is not in the middle of a task, eg killing a zombie
        //TODO check to ensure the NPC is not needed for some other task, eg helping another NPC from dieing
        Pos pos = PositionGenerator.randomPointTowards(entity(), 16, 7, new Pos(getPoint()));
        if (pos != null)
        {
            entity().getNavigator().tryMoveToXYZ(pos.x(), pos.y(), pos.z(), entity().getAIMoveSpeed());
        }
    }

    public IPos3D getPoint()
    {
        return point;
    }

    public void setPoint(IPos3D point)
    {
        this.point = point;
    }
}
