package com.builtbroken.artillects.api;

import com.builtbroken.artillects.core.entity.passive.Profession;
import com.builtbroken.artillects.core.zone.Zone;

/**
 * Aplied to entities that function as a worker for a zoned area
 *
 * @author Darkguardsman
 */
public interface IWorker
{
    /** Current profession the worker is applying
     * @return Object that describes what the worker does, or rather the skills the work has
     */
    Profession getProfession();

    /** Current area the worker is assigned to, if the work is assigned to several areas return the current one.
     * @return Current zone the worker is in
     */
    Zone getWorkingZone();

    /**
     * Sets the zone
     * @param zone - zone to assign this worker to. Can be null
     */
    void setWorkingZone(Zone zone);
}
