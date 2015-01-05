package artillects.content.potion;

import com.builtbroken.mc.prefab.potion.CustomPotion;
import net.minecraft.potion.Potion;

/** Prefab for all medical based potion effects
 *
 * @author Darkguardsman
 */
public abstract class PotionMedical extends CustomPotion
{
    public static final PotionBleeding BLEEDING = new PotionBleeding();
    public static final PotionInfection INFECTION = new PotionInfection("Infection");
    public static final PotionBloodPoisoning BLOOD_POISONING = new PotionBloodPoisoning(); //septicemia
    public static final PotionTetanus TETANUS = new PotionTetanus();

    /** Rate by which the effect is ready for ticking */
    protected int tickRate = 20;

    public PotionMedical(boolean isBadEffect, int color, String name)
    {
        super(getNextId(), isBadEffect, color, "Medical_" + name);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration % tickRate == 0;
    }
}
