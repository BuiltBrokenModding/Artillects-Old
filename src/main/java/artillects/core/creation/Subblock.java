package artillects.core.creation;

import net.minecraft.util.Icon;
import resonant.lib.render.RenderUtility;

/** Metadata sub version of a block
 * 
 * @author Darkguardsman */
public class Subblock
{
    public float hardness = 1;
    public float resistance = 1;
    public boolean hasSides = false;
    public String unlocalizedName;
    public String iconName;
    public Icon iconMain = null;
    public Icon[] iconSide;
    public String[] iconSideName;

    public void addSideIcon(int side, String name)
    {
        this.hasSides = true;
        if (this.iconSideName == null)
            this.iconSideName = new String[6];
        this.iconSideName[side] = name;
    }

    public Icon getIcon(int side)
    {
        if (hasSides && iconSideName != null)
        {
            if (iconSide == null)
                iconSide = new Icon[6];

            if (iconSide[side] == null)
            {
                if (ContentLoader.blockTextures.containsKey(iconSideName[side]))
                {
                    iconSide[side] = ContentLoader.blockTextures.get(iconSideName[side]);
                }
                else if (RenderUtility.getIcon(iconSideName[side]) != null)
                {
                    iconSide[side] = RenderUtility.getIcon(iconSideName[side]);
                }
            }
            if (iconSide[side] != null)
            {
                return iconSide[side];
            }
        }
        if (iconMain == null)
        {
            if (ContentLoader.blockTextures.containsKey(iconName))
            {
                iconMain = ContentLoader.blockTextures.get(iconName);
            }
            else if (RenderUtility.getIcon(iconName) != null)
            {
                iconMain = RenderUtility.getIcon(iconName);
            }
        }
        return iconMain;
    }

}