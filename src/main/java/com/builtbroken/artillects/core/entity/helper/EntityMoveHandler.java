package com.builtbroken.artillects.core.entity.helper;

import com.builtbroken.artillects.core.entity.EntityArtillect;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

@Deprecated
public class EntityMoveHandler
{
    /** The EntityLiving that is being moved */
    private EntityArtillect entity;
    private double posX;
    private double posY;
    private double posZ;
    /** The speed at which the entity should move */
    private double speed;
    private boolean update;

    public EntityMoveHandler(EntityArtillect p_i1614_1_)
    {
        this.entity = p_i1614_1_;
        this.posX = p_i1614_1_.posX;
        this.posY = p_i1614_1_.posY;
        this.posZ = p_i1614_1_.posZ;
    }

    public boolean isUpdating()
    {
        return this.update;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    /**
     * Sets the speed and location to move to
     */
    public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_)
    {
        this.posX = p_75642_1_;
        this.posY = p_75642_3_;
        this.posZ = p_75642_5_;
        this.speed = p_75642_7_;
        this.update = true;
    }

    public void onUpdateMoveHelper()
    {
        //Reset move speed
        this.entity.setMoveForward(0.0f);
        if (this.update)
        {
            this.update = false;
            int yMidPoint = MathHelper.floor_double(this.entity.boundingBox.minY + 0.5D);
            double deltaX = this.posX - this.entity.posX;
            double deltaZ = this.posZ - this.entity.posZ;
            double deltaY = this.posY - (double) yMidPoint;
            double mag = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

            //If too far from move point, set speed and move
            if (mag >= 2.500000277905201E-7D)
            {
                float f = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 30.0F);
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

                if (deltaY > 0.0D && deltaX * deltaX + deltaZ * deltaZ < 1.0D)
                {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }

    /**
     * Limits the given angle to a upper and lower limit.
     */
    private float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_)
    {
        float f3 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);

        if (f3 > p_75639_3_)
        {
            f3 = p_75639_3_;
        }

        if (f3 < -p_75639_3_)
        {
            f3 = -p_75639_3_;
        }

        return p_75639_1_ + f3;
    }
}