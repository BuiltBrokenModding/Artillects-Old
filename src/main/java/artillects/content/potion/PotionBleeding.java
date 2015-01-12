package artillects.content.potion;

import com.builtbroken.mc.core.content.damage.DamageSources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Potion effect designed to cause the entity to bleed out slowly
 * Created on 10/4/2014.
 *
 * @author Darkguardsman
 */
public class PotionBleeding extends PotionMedical
{
    public PotionBleeding()
    {
        super(true, 8271127, "Bleeding");
        this.setIconIndex(6, 0);
    }

    @Override
    public void performEffect(EntityLivingBase ent, int amplifier)
    {
        super.performEffect(ent, amplifier);
        if (ent != null && ent.isPotionActive(this))
        {
            if(ent instanceof EntityPlayer)
            {
                ((EntityPlayer) ent).addChatComponentMessage(new ChatComponentText("Bleeding..."));
            }
            ent.attackEntityFrom(DamageSources.BLEEDING.getSource(), 0.5f + 0.125f * ent.worldObj.difficultySetting.getDifficultyId());
            ent.worldObj.spawnParticle("reddust", ent.posX, ent.posY, ent.posZ, 0, -0.1, 0);
            if (ent.worldObj.rand.nextFloat() <= 0.005f)
            {
                if(ent instanceof EntityPlayer)
                {
                    ((EntityPlayer) ent).addChatComponentMessage( new ChatComponentText("Bleeding has stopped"));
                }
                ent.removePotionEffect(BLEEDING.getId());
            }
        }
    }
}
