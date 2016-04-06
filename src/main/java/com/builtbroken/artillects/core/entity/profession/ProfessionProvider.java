package com.builtbroken.artillects.core.entity.profession;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/5/2016.
 */
public abstract class ProfessionProvider<P extends Profession>
{
    public abstract P newProfession(Entity entity);

    public abstract P loadFromSave(Entity entity, NBTTagCompound compound);
}
