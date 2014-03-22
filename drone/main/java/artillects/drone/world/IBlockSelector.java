package artillects.drone.world;

import universalelectricity.api.vector.VectorWorld;

/** Call back interface for AreaScanner when it looks at each block
 * 
 * @author Darkguardsman */
public interface IBlockSelector
{
    public void onScan(final VectorWorld loc);
}
