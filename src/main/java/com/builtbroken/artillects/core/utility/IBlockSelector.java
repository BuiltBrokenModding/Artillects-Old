package com.builtbroken.artillects.core.utility;

import com.builtbroken.mc.lib.transform.vector.Location;

/** Call back interface for AreaScanner when it looks at each block
 * 
 * @author Darkguardsman */
public interface IBlockSelector
{
    void onScan(final Location loc);
}
