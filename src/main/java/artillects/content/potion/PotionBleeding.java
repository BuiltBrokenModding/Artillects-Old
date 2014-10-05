package artillects.content.potion;

import artillects.Settings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        if (ent.worldObj.rand.nextFloat() > 0.9 - (amplifier * 0.07))
        {
            ent.setHealth(ent.getHealth() - 0.5f * ent.worldObj.difficultySetting.getDifficultyId());
            ent.worldObj.spawnParticle("reddust", ent.posX, ent.posY, ent.posZ, 0, -0.1, 0);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        if (duration % 2 == 0)
        {
            return true;
        }
        return false;
    }
}
