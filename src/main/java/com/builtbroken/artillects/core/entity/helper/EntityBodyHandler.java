package com.builtbroken.artillects.core.entity.helper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@Deprecated
public class EntityBodyHandler
{
    /** Instance of EntityLiving. */
    private EntityLivingBase host;
    private int ticks;
    private float previousYawHead;

    public EntityBodyHandler(EntityLivingBase host)
    {
        this.host = host;
    }

    public void func_75664_a()
    {
        double deltaX = this.host.posX - this.host.prevPosX;
        double deltaZ = this.host.posZ - this.host.prevPosZ;

        if (deltaX * deltaX + deltaZ * deltaZ > 2.500000277905201E-7D)
        {
            this.host.renderYawOffset = this.host.rotationYaw;
            this.host.rotationYawHead = this.clamp(this.host.renderYawOffset, this.host.rotationYawHead, 75.0F);
            this.previousYawHead = this.host.rotationYawHead;
            this.ticks = 0;
        }
        else
        {
            float rotationLimit = 75.0F;

            if (Math.abs(this.host.rotationYawHead - this.previousYawHead) > 15.0F)
            {
                this.ticks = 0;
                this.previousYawHead = this.host.rotationYawHead;
            }
            else
            {
                ++this.ticks;

                if (this.ticks > 10)
                {
                    rotationLimit = Math.max(1.0F - (float)(this.ticks - 10) / 10.0F, 0.0F) * 75.0F;
                }
            }

            this.host.renderYawOffset = this.clamp(this.host.rotationYawHead, this.host.renderYawOffset, rotationLimit);
        }
    }

    private float clamp(float renderYawOffset, float rotationYawHead, float angleLimit)
    {
        float angle = MathHelper.wrapAngleTo180_float(renderYawOffset - rotationYawHead);

        if (angle < -angleLimit)
        {
            angle = -angleLimit;
        }

        if (angle >= angleLimit)
        {
            angle = angleLimit;
        }

        return renderYawOffset - angle;
    }
}