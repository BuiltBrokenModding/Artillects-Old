package com.builtbroken.artillects.core.entity;

import com.builtbroken.artillects.core.entity.ai.AITaskList;
import com.builtbroken.artillects.core.entity.helper.*;
import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
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
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Recoded version of EntityLivingBase to act as the prefab for any
 * entity that has an AI like functionality.
 */
public abstract class EntityArtillect<I extends IInventory> extends EntityLivingBase implements IInventoryProvider
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
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(10, "");
    }

    /**
     * Gets called every tick from main Entity class
     */
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks()
    {
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

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setString("CustomName", this.getCustomNameTag());
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
    }

    /**
     * set the movespeed used for the new AI system
     */
    @Override
    public void setAIMoveSpeed(float speed)
    {
        super.setAIMoveSpeed(speed);
        this.moveForward = speed;
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
    public final boolean interactFirst(EntityPlayer player)
    {
        return super.interactFirst(player);
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
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
    public boolean isUsingMeeleWeapon()
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
}