package artillects.content.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by robert on 10/9/2014.
 */
public class PotionBloodPoisoning extends PotionInfection
{
    public PotionBloodPoisoning()
    {
        super("BloodPoisoning");
    }

    @Override
    public void performEffect(EntityLivingBase ent, int amplifier)
    {
        super.performEffect(ent, amplifier);
        if (ent != null && ent.isPotionActive(this))
        {
            if (ent.getHealth() > (ent.getMaxHealth() * .2f))
            {
                ent.setHealth(ent.getMaxHealth() * .2f);
            }
        }
    }
}
