package artillects.content.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import resonant.lib.utility.dice.DiceRoller;

/**
 * Created by robert on 10/9/2014.
 */
public class PotionInfection extends PotionMedical
{
    protected boolean canEntityKillInfection = true;
    protected boolean canDevelopeIntoBloodPoisoning = true;

    protected float chanceToKillInfection = 0.005f;
    protected float chanceToCauseBloodPoisoning = 0.003f;

    protected DiceRoller infection_roller = new DiceRoller(6, 6, 5, 6, 6);
    protected DiceRoller poisoning_roller = new DiceRoller(10, 6, 5, 6, 10);

    public PotionInfection(String name)
    {
        super(true, 2913139, name);
    }

    @Override
    public void performEffect(EntityLivingBase ent, int amplifier)
    {
        super.performEffect(ent, amplifier);
        if (ent != null && ent.isPotionActive(this))
        {
            // Always apply weakness to simulator the character is sick
            if (!ent.isPotionActive(Potion.weakness))
            {
                ent.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 1200, amplifier));
            }

            //Chance to remove potion effect, simulator the body fighting back
            if (canEntityKillInfection && chanceRemoveInfection())
            {
                ent.removePotionEffect(INFECTION.getId());
                ((EntityPlayer) ent).addChatComponentMessage( new ChatComponentText("Infection has been fought off"));
                return;
            }

            //Chance to apply additional effects
            else if (canDevelopeIntoBloodPoisoning && chanceAddBloodPoisoning())
            {
                if (!ent.isPotionActive(BLOOD_POISONING))
                {
                    ent.removePotionEffect(INFECTION.getId());
                    ent.addPotionEffect(new PotionEffect(BLOOD_POISONING.getId(), 50000, 2));
                    ((EntityPlayer) ent).addChatComponentMessage( new ChatComponentText("Infection has turned into blood poisoning"));
                }
            }
        }
    }

    /** Chance(internal chance calculation) that the infection will be removed
     * @return true if it should be removed */
    public boolean chanceRemoveInfection()
    {
        return infection_roller.roll();
    }

    /** Chance(internal chance calculation) that the infection will turn into blood
     * @return true if it should be add */
    public boolean chanceAddBloodPoisoning()
    {
        return poisoning_roller.roll();
    }
}
