package com.builtbroken.artillects.core.creation;

import net.minecraft.util.IIcon;

/** Metadata sub version of a block
 * 
 * @author Darkguardsman */
public class Subblock
{
    public float hardness = -2;
    public float resistance = -2;
    public boolean hasSides = false;
    public String unlocalizedName;
    public String iconName;
    public IIcon iconMain = null;
    public IIcon[] iconSide;
    public String[] iconSideName;

    public void addSideIcon(int side, String name)
    {
        if (side == -1)
        {
            this.iconName = name;
        }
        else
        {
            this.hasSides = true;
            if (this.iconSideName == null)
                this.iconSideName = new String[6];
            this.iconSideName[side] = name;
        }
    }

    public IIcon getIcon(int side)
    {
        if (hasSides && iconSideName != null)
        {
            if (iconSide == null)
                iconSide = new IIcon[6];
            
            if (iconSide[side] != null)
            {
                return iconSide[side];
            }
        }
        return iconMain;
    }

}