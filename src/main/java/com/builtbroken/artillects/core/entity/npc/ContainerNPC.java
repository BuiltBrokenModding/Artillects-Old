package com.builtbroken.artillects.core.entity.npc;

import com.builtbroken.mc.prefab.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2016.
 */
public class ContainerNPC extends ContainerBase
{
    public ContainerNPC(EntityPlayer player, EntityNpc npc)
    {
        super(player, npc);
        addSlotToContainer(new Slot(npc, 1, 10, 17 * 4 - 5));
        addSlotToContainer(new Slot(npc, 2, 10, 17 * 3 - 5));
        addSlotToContainer(new Slot(npc, 3, 10, 17 * 2 - 5));
        addSlotToContainer(new Slot(npc, 4, 10, 12));
        addSlotToContainer(new Slot(npc, 0, 27, 12));
        for(int i = 0; i < 5; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                addSlotToContainer(new Slot(npc, i + 5, 70 + 17 * i, 17 * j + 12));
            }
        }
        addPlayerInventory(player);
    }
}
