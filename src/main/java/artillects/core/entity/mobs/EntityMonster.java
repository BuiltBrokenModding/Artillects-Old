package artillects.core.entity.mobs;

import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import artillects.core.entity.EntityBase;

public class EntityMonster extends EntityBase implements IMob
{
    public EntityMonster(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0)
        {
            this.setDead();
        }
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        return super.attackEntityFrom(source, damage);
    }
}
