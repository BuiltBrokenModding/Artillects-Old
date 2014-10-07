package artillects.content.potion;

import artillects.Settings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import resonant.lib.prefab.damage.CustomDamageSource;
import resonant.lib.prefab.poison.PoisonRadiation;
import resonant.lib.prefab.poison.PotionRadiation;
import resonant.lib.prefab.potion.CustomPotion;

/** Potion effect designed to cause the entity to bleed out slowly
 * Created on 10/4/2014.
 * @author Darkguardsman
 */
public class PotionBleeding extends CustomPotion
{
    public static final PotionBleeding INSTANCE = new PotionBleeding(33);
    public static final PotionBleeding INFECTION = new PotionBleeding(34);
    public static final PotionBleeding BLOOD_POISONING = new PotionBleeding(34); //septicemia

    public static final DamageSource BLEED_DAMAGE = new CustomDamageSource("BleedOut").setDamageBypassesArmor().setDamageIsAbsolute();

    public PotionBleeding(int id)
    {
        super(id, true, 8271127, "bleeding");
        this.setIconIndex(6, 0);
    }

    @Override
    public void performEffect(EntityLivingBase ent, int amplifier)
    {
        if(ent instanceof EntityPlayer && ((EntityPlayer)ent).capabilities.isCreativeMode)
        {
            ent.removePotionEffect(INSTANCE.getId());
            return;
        }
        if(ent.worldObj.difficultySetting.getDifficultyId() == 0 || !Settings.ENABLE_BLEEDING || ent.getHealth() <= 0)
        {
            ent.removePotionEffect(INSTANCE.getId());
            return;
        }
        if(this.getId() == INFECTION.getId())
        {
            if(!ent.isPotionActive(Potion.weakness))
            {
                ent.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 1200));
            }
            if(ent.worldObj.rand.nextFloat() <= 0.05f)
            {
                ent.removePotionEffect(INFECTION.getId());
            }
            else if(ent.worldObj.rand.nextFloat() <= 0.03f)
            {
                if(!ent.isPotionActive(BLOOD_POISONING))
                {
                    ent.removePotionEffect(INFECTION.getId());
                    ent.addPotionEffect(new PotionEffect(BLOOD_POISONING.getId(), 50000, 2));
                }
            }
        }
        else if(this.getId() == BLOOD_POISONING.getId())
        {
            if(!ent.isPotionActive(Potion.weakness))
            {
                ent.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 1200, 2));
            }
            if(ent.getHealth() > (ent.getMaxHealth() * .2f))
            {
                ent.setHealth(ent.getMaxHealth() * .2f);
            }
        }
        else if(this.getId() == INSTANCE.getId())
        {
            ent.attackEntityFrom(BLEED_DAMAGE, ent.getHealth() - 0.5f * ent.worldObj.difficultySetting.getDifficultyId());
            ent.worldObj.spawnParticle("reddust", ent.posX, ent.posY, ent.posZ, 0, -0.1, 0);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration % 20 == 0;
    }
}
