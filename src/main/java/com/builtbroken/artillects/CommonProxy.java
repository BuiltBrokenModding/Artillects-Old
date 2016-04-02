package com.builtbroken.artillects;

import com.builtbroken.mc.lib.mod.AbstractProxy;

public class CommonProxy extends AbstractProxy
{
    @Override
    public void preInit()
    {
        super.preInit();
        loadServerData();
    }

    public void loadServerData()
    {

    }
}
