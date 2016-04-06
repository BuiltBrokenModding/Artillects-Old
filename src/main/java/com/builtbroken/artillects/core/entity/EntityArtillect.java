package com.builtbroken.artillects.core.entity;

import com.builtbroken.artillects.core.entity.ai.AITaskList;
import com.builtbroken.artillects.core.entity.helper.*;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.ISave;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Recoded version of EntityLivingBase to act as the prefab for any
 * entity that has an AI like functionality.
 */
public abstract class EntityArtillect<I extends IInventory> extends EntityLivingBase implements IInventoryProvider, IWorldPosition, IPos3D
{
    /** The experience points the Entity gives. */
    protected int experienceValue;

    public EntityAimHandler lookHelper;
    public EntityMoveHandler moveHelper;
    /** Entity jumping helper */
    private EntityJumpHandler jumpHelper;
    private EntityBodyHandler bodyHelper;
    private PathNavigate2 navigator;

    /** List of task to execute per tick */
    public final AITaskList tasks;
    /** The active target the Task system uses for tracking */
    private EntityLivingBase attackTarget;

    /** Internal inventory used by the entity */
    protected I inventory;

    /** Used by AI task to ignore check for weapons, only set if AI needs to fight but has no weapon */
    public boolean ignoreWeaponCheck = false;

    public EntityArtillect(World world)
    {
        super(world);
        this.tasks = new AITaskList();
        this.lookHelper = new EntityAimHandler(this);
        this.moveHelper = new EntityMoveHandler(this);
        this.jumpHelper = new EntityJumpHandler(this);
        this.bodyHelper = new EntityBodyHandler(this);
        this.navigator = new PathNavigate2(this, world);
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(10, "");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        return super.attackEntityFrom(source, damage);
    }

    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();
        ++this.entityAge;
        this.tasks.onUpdateTasks();
        this.navigator.onUpdateNavigation();
        this.updateAITick();
        this.moveHelper.onUpdateMoveHelper();
        this.lookHelper.onUpdateLook();
        this.jumpHelper.doJump();
    }

    /**
     * Get the experience points the entity currently has.
     */
    @Override
    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        return this.experienceValue;
    }

    @Override
    protected float func_110146_f(float p_110146_1_, float p_110146_2_)
    {
        this.bodyHelper.func_75664_a();
        return p_110146_2_;
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;

        if (p_70652_1_ instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) p_70652_1_);
            i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) p_70652_1_);
        }

        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                p_70652_1_.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                p_70652_1_.setFire(j * 4);
            }

            if (p_70652_1_ instanceof EntityLivingBase)
            {
                EnchantmentHelper.func_151384_a((EntityLivingBase) p_70652_1_, this);
            }

            EnchantmentHelper.func_151385_b(this, p_70652_1_);
        }

        return flag;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity p_70785_1_, float p_70785_2_)
    {
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setString("CustomName", this.getCustomNameTag());
        if (inventory instanceof ISave)
        {
            NBTTagCompound tag = new NBTTagCompound();
            ((ISave) inventory).save(tag);
            nbt.setTag("inventory", tag);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("CustomName", 8) && nbt.getString("CustomName").length() > 0)
        {
            this.setCustomNameTag(nbt.getString("CustomName"));
        }
        if (inventory instanceof ISave && nbt.hasKey("inventory"))
        {
            ((ISave) inventory).load(nbt.getCompoundTag("inventory"));
        }
    }

    @Override
    public void setAIMoveSpeed(float p_70659_1_)
    {
        super.setAIMoveSpeed(p_70659_1_);
        this.setMoveForward(p_70659_1_);
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setMoveForward(float p_70657_1_)
    {
        this.moveForward = p_70657_1_;
    }


    public EntityAimHandler getLookHelper()
    {
        return this.lookHelper;
    }

    public EntityMoveHandler getMoveHelper()
    {
        return this.moveHelper;
    }

    public EntityJumpHandler getJumpHelper()
    {
        return this.jumpHelper;
    }

    public PathNavigate2 getNavigator()
    {
        return this.navigator;
    }


    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        return this.attackTarget;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase p_70624_1_)
    {
        this.attackTarget = p_70624_1_;
        ForgeHooks.onLivingSetAttackTarget(this, p_70624_1_);
    }

    /**
     * The number of iterations PathFinder.getSafePoint will execute before giving up.
     */
    @Override
    public int getMaxSafePointTries()
    {
        if (this.getAttackTarget() == null)
        {
            return 3;
        }
        else
        {
            int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);
            i -= (3 - this.worldObj.difficultySetting.getDifficultyId()) * 4;

            if (i < 0)
            {
                i = 0;
            }

            return i + 3;
        }
    }

    @Override
    public String getCommandSenderName()
    {
        return this.hasCustomNameTag() ? this.getCustomNameTag() : super.getCommandSenderName();
    }

    public void setCustomNameTag(String p_94058_1_)
    {
        this.dataWatcher.updateObject(10, p_94058_1_);
    }

    public String getCustomNameTag()
    {
        return this.dataWatcher.getWatchableObjectString(10);
    }

    public boolean hasCustomNameTag()
    {
        return this.dataWatcher.getWatchableObjectString(10).length() > 0;
    }


    /**
     * First layer of player interaction
     */
    @Override
    public boolean interactFirst(EntityPlayer player)
    {
        return super.interactFirst(player);
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    @Override
    public World world()
    {
        return worldObj;
    }

    @Override
    public double x()
    {
        return posX;
    }

    @Override
    public double y()
    {
        return posY;
    }

    @Override
    public double z()
    {
        return posZ;
    }

    //------------------------------------------
    //--------- Inventory Code -----------------
    //------------------------------------------

    @Override
    public I getInventory()
    {
        return inventory;
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side)
    {
        return true;
    }

    @Override
    public ItemStack getHeldItem()
    {
        return getEquipmentInSlot(0);
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot)
    {
        return getInventory() != null ? getInventory().getStackInSlot(slot) : null;
    }

    /**
     * Drop the equipment for this entity.
     */
    @Override
    protected void dropEquipment(boolean recentlyHit, int lootMod)
    {
        if (getInventory() != null)
        {
            for (int slot = 0; slot < this.getInventory().getSizeInventory(); ++slot)
            {
                ItemStack itemstack = this.getInventory().getStackInSlot(slot);
                if (itemstack != null && itemstack.getItem() != null && itemstack.stackSize > 0)
                {
                    InventoryUtility.dropItemStack(worldObj, posX, posY, posZ, itemstack, 10, 0);
                }
                this.getInventory().setInventorySlotContents(slot, null);
            }
        }
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack)
    {
        //TODO implement in NPC class
    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        //TODO implement in later classes
        return null;
    }

    /**
     * Is the held item a ranged weapon
     *
     * @return true if the item is a ranged weapon, or can be used as a ranged weapon
     */
    public boolean isUsingRangedWeapon()
    {
        //TODO add handler for implementing modded weapons
        return getHeldItem() != null && getHeldItem().getItem() instanceof ItemBow;
    }

    /**
     * Is the held item a meele weapon
     *
     * @return
     */
    public boolean isUsingMeleeWeapon()
    {
        //TODO add handler for implementing modded weapons
        return getHeldItem() != null && (getHeldItem().getItem() instanceof ItemSword || getHeldItem().getItem() instanceof ItemTool);
    }

    /**
     * Is the item held a weapon
     *
     * @return true if the item is a weapon, and does damage when hitting entities. Exclude blocks and crafting items.
     */
    public boolean hasWeapon()
    {
        //TODO add handler for implementing modded weapons
        if (getHeldItem() != null)
        {
            return getHeldItem().getItem() instanceof ItemSword || getHeldItem().getItem() instanceof ItemBow || getHeldItem().getItem() instanceof ItemTool;
        }
        return false;
    }

    //------------------------------------------
    //--------- OLD MC CODE, NOT USED ----------
    //------------------------------------------
    @Override
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.moveStrafing = 0.0F;
        this.moveForward = 0.0F;

        boolean flag1 = this.isInWater();
        boolean flag = this.handleLavaMovement();

        if (flag1 || flag)
        {
            this.isJumping = this.rand.nextFloat() < 0.8F;
        }
    }

    @Deprecated
    public ItemStack func_130225_q(int slot)
    {
        return getEquipmentInSlot(slot + 1);
    }
}