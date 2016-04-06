package com.builtbroken.artillects.core.entity.npc;

import com.builtbroken.artillects.core.entity.EntityHumanoid;
import com.builtbroken.artillects.core.entity.ai.AITaskSwimming;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskFindTarget;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMeleeAttack;
import com.builtbroken.artillects.core.entity.ai.combat.AITaskMoveTowardsTarget;
import com.builtbroken.artillects.core.entity.ai.npc.NpcTaskReturnHome;
import com.builtbroken.artillects.core.entity.profession.Profession;
import com.builtbroken.artillects.core.entity.profession.ProfessionProvider;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionGuard;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticle;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticleCircle;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.vector.Location;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntityLivingSelector;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Prefab for all NPC that artillects will contain including crafters and fighters
 *
 * @author Darkguardsman
 */
public class EntityNpc extends EntityHumanoid<InventoryNPC> implements INpc, IEntityAdditionalSpawnData, IPacketIDReceiver
{
    /** Map of profession ids to profession creation objects, for internal use only... will be moved to a handler later */
    public static HashMap<String, ProfessionProvider> registeredProfessions = new HashMap();

    /** Object that handles finer details of what the NPC is, and does */
    private Profession profession;
    /** ID to load NPC's profession from disk, also may be used for render and other references */
    protected String professionID = "citizen"; //Default profession with zero tasks

    /** Location of this NPCs home, defaults to spawn position. Is only used as a back up to an action home object point. */
    private Location homeLocation;

    private boolean loadedProfessionAITasks = false;

    /** Toggled to render data about the NPC in the world */
    private boolean renderData = false;

    public EntityNpc(World world)
    {
        super(world);
        inventory = new InventoryNPC(this);
        //AI tasks
        this.tasks.add(0, new AITaskSwimming(this));
        this.tasks.add(2, new AITaskFindTarget(this, new EntityLivingSelector().selectMobs()));
        this.tasks.add(3, new AITaskMoveTowardsTarget(this, 1.0f));
        this.tasks.add(4, new AITaskMeleeAttack(this));
        this.tasks.add(7, new NpcTaskReturnHome(this));
    }

    @Override
    protected void updateAITasks()
    {
        //Load profession before any other AI task is done
        if (getProfession() == null && registeredProfessions.get(professionID) != null)
        {
            setProfession(registeredProfessions.get(professionID).newProfession(this));
        }
        //Ensure AI tasks are loaded
        if (!loadedProfessionAITasks)
        {
            loadedProfessionAITasks = true;
            getProfession().loadAITasks();
        }
        //Run update on AI in case any AI tasks need updated
        getProfession().update();
        //Then run any other AI calls
        super.updateAITasks();
        if (renderData)
        {
            renderDataInWorld();
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer player)
    {
        if (!super.interactFirst(player))
        {
            if (Engine.runningAsDev && player.getHeldItem() != null && player.getHeldItem().getItem() == Items.stick)
            {
                renderData = !renderData;
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Called to render visual data about the NPC. Such
     * as home position, current task, zone bounds,
     * path to target, etc.
     * <p/>
     * Called server side so use this to send
     * packet data for actual render calls.
     */
    protected void renderDataInWorld()
    {
        if (homeLocation != null)
        {
            Engine.instance.packetHandler.sendToAllAround(new PacketSpawnParticle("smoke", worldObj.provider.dimensionId, homeLocation.x(), homeLocation.y(), homeLocation.z(), 0, -0.1f, 0), (IWorldPosition) this, 70);
        }
        if (getProfession() instanceof ProfessionGuard)
        {
            if (((ProfessionGuard) getProfession()).centerOfGuardZone != null)
            {
                Pos pos = ((ProfessionGuard) getProfession()).centerOfGuardZone;
                Engine.instance.packetHandler.sendToAllAround(new PacketSpawnParticle("flame", worldObj.provider.dimensionId, pos.x(), pos.y() + 1, pos.z(), 0, -0.1f, 0), (IWorldPosition) this, 70);
                Engine.instance.packetHandler.sendToAllAround(new PacketSpawnParticleCircle("flame", worldObj.provider.dimensionId, pos, ((ProfessionGuard) getProfession()).zoneDistance), (IWorldPosition) this, Math.max(30 + ((ProfessionGuard) getProfession()).zoneDistance, 70));
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setString("professionID", professionID);
        if (getProfession() != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            getProfession().save(tag);
            nbt.setTag("professionSave", tag);
        }
        if (homeLocation != null)
        {
            nbt.setTag("homeLocation", homeLocation.toNBT());
        }
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("professionID"))
        {
            this.professionID = nbt.getString("professionID");
        }
        if (professionID != null && registeredProfessions.get(professionID) != null)
        {
            if (nbt.hasKey("professionSave"))
            {
                setProfession(registeredProfessions.get(professionID).loadFromSave(this, nbt.getCompoundTag("professionSave")));
            }
            else
            {
                setProfession(registeredProfessions.get(professionID).newProfession(this));
            }
        }
        if (nbt.hasKey("homeLocation"))
        {
            this.homeLocation = new Location(nbt.getCompoundTag("homeLocation"));
        }
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack)
    {
        if (slot >= 0 && slot < 5)
        {
            if (inventory != null)
            {
                inventory.setInventorySlotContents(slot, stack);
            }
            else
            {
                Engine.error("EntityNPC: inventory is null so can't set " + stack + " into slot " + slot + "\nEntity = '" + this + "'");
            }
        }
        else
        {
            Engine.error("EntityNPC: something tried to set " + stack + " into slot " + slot + " which is outside the 0 - 5 limit for gear. \nEntity = '" + this + "'");
        }
    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return inventory != null ? inventory.gear : null;
    }

    @Override
    public ItemStack getHeldItem()
    {
        return inventory != null ? inventory.getHeldItem() : null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot)
    {
        return inventory != null && slot >= 0 && slot < 5 ? inventory.gear[slot] : null;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(inventory != null);
        if (inventory != null)
        {
            writeGearData(buffer);
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        //Prevent reading server side
        if (worldObj.isRemote)
        {
            if (additionalData.readBoolean())
            {
                readGearData(additionalData);
            }
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType packet)
    {
        if (worldObj.isRemote)
        {
            if (id == 1)
            {
                readGearData(buf);
                return true;
            }
        }
        return false;
    }

    protected void readGearData(ByteBuf buf)
    {
        for (int i = 0; i < inventory.gear.length; i++)
        {
            inventory.gear[i] = null;
            NBTTagCompound tag = ByteBufUtils.readTag(buf);
            if (tag != null && !tag.hasNoTags())
            {
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                if (stack != null)
                {
                    inventory.gear[i] = stack;
                }
            }
        }
    }

    protected void writeGearData(ByteBuf buf)
    {
        for (ItemStack stack : inventory.gear)
        {
            if (stack != null)
            {
                ByteBufUtils.writeTag(buf, stack.writeToNBT(new NBTTagCompound()));
            }
            else
            {
                ByteBufUtils.writeTag(buf, new NBTTagCompound());
            }
        }
    }

    /** Location of this NPCs home, defaults to spawn position */
    public Location getHomeLocation()
    {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation)
    {
        this.homeLocation = homeLocation;
    }

    public float getBlockPathWeight(Pos pos)
    {
        return 0.0F;
    }

    /**
     * Gets the profession object used to control
     * most of the AI task for this NPC. Do not
     * modify the profession unless you know
     * what your doing.
     *
     * @return profession if one is set
     */
    public Profession getProfession()
    {
        return profession;
    }

    /**
     * Called to change the profession of the NPC.
     * Will clear some AI tasks and load new task on next tick
     *
     * @param profession
     */
    public void setProfession(Profession profession)
    {
        //Ensure that profession AI tasks load
        if (this.profession == null || this.profession != profession)
        {
            loadedProfessionAITasks = false;
        }
        //Clear old AI tasks
        if (this.profession != null)
        {
            profession.unloadAITask();
        }
        this.profession = profession;
    }
}
