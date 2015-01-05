package artillects.core.utility;

import com.builtbroken.mc.lib.transform.vector.VectorWorld;

/** Call back interface for AreaScanner when it looks at each block
 * 
 * @author Darkguardsman */
public interface IBlockSelector
{
    public void onScan(final VectorWorld loc);
}
