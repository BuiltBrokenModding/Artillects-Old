package com.builtbroken.artillects.core.entity.ai;

import com.builtbroken.artillects.core.entity.passive.EntityNpc;
import com.builtbroken.mc.lib.transform.vector.Pos;
import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * Helper class used to generate random positions for AI scripts.
 * Some code is cloned from {@link net.minecraft.entity.ai.RandomPositionGenerator} which can't
 * be used as its coded for {@link net.minecraft.entity.EntityCreature} only.
 */
public final class PositionGenerator
{
    /**
     * Finds a random point in an area
     *
     * @param entity
     * @param distanceBlocks
     * @param yDistanceBlocks
     * @return
     */
    public static Pos randomPoint(Entity entity, int distanceBlocks, int yDistanceBlocks)
    {
        return randomPoint(entity, distanceBlocks, yDistanceBlocks, null);
    }

    /**
     * Generates a random position towards the target point. Tries 10 times to find a position before returning null.
     *
     * @param entity          - entity used as center point, and check for block weight
     * @param distanceBlocks  - distance in x and z to move
     * @param yDistanceBlocks - distance in y to move
     * @param target          - target to move towards
     * @return position, or null if none were found
     */
    public static Pos randomPointTowards(Entity entity, int distanceBlocks, int yDistanceBlocks, Pos target)
    {
        return randomPoint(entity, distanceBlocks, yDistanceBlocks, target.sub(entity.posX, entity.posY, entity.posZ));
    }

    /**
     * Generates a random position away from the target point. Tries 10 times to find a position before returning null.
     *
     * @param entity          - entity used as center point, and check for block weight
     * @param distanceBlocks  - distance in x and z to move
     * @param yDistanceBlocks - distance in y to move
     * @param target          - target to move away from
     * @return position, or null if none were found
     */
    public static Pos randomPointAway(Entity entity, int distanceBlocks, int yDistanceBlocks, Pos target)
    {
        return randomPoint(entity, distanceBlocks, yDistanceBlocks, new Pos(entity).sub(target));
    }

    /**
     * Finds a block position in the given direction. Tries 10 times to find a valid position before returning null
     *
     * @param entity          - entity to use as a center point, and valid block weights
     * @param distanceBlocks  - distance x and z
     * @param yDistanceBlocks - distance y
     * @param direction       - direction to move in, null will be all directions
     * @return position or null if none were found
     */
    public static Pos randomPoint(Entity entity, int distanceBlocks, int yDistanceBlocks, Pos direction)
    {
        final Random random = com.builtbroken.jlib.helpers.MathHelper.rand;
        Pos bestPos = null;
        float bestValue = -99999.0F;

        for (int tries = 0; tries < 10; ++tries)
        {
            //Generate random position inside out limits
            Pos randomPos = new Pos(random.nextInt(2 * distanceBlocks) - distanceBlocks, random.nextInt(2 * yDistanceBlocks) - yDistanceBlocks, random.nextInt(2 * distanceBlocks) - distanceBlocks);

            if (direction == null || randomPos.x() * direction.xi() + randomPos.z() * direction.zi() >= 0.0D) //TODO figure out what this math is doing, assumed it is checking direction
            {
                randomPos = randomPos.add(entity.posX, entity.posY, entity.posZ);

                float blockValue = entity instanceof EntityNpc ? ((EntityNpc) entity).getBlockPathWeight(randomPos) : 0;

                if (blockValue > bestValue)
                {
                    bestValue = blockValue;
                    bestPos = randomPos;
                }
            }
        }
        return bestPos;
    }
}