package com.builtbroken.artillects.core.entity.helper;

import com.builtbroken.artillects.core.entity.EntityArtillect;

@Deprecated
public class EntityJumpHandler
{
    private EntityArtillect entity;
    private boolean isJumping;
    private static final String __OBFID = "CL_00001571";

    public EntityJumpHandler(EntityArtillect p_i1612_1_)
    {
        this.entity = p_i1612_1_;
    }

    public void setJumping()
    {
        this.isJumping = true;
    }

    /**
     * Called to actually make the entity jump if isJumping is true.
     */
    public void doJump()
    {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}