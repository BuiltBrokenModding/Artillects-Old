package artillects.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import universalelectricity.core.transform.vector.IVectorWorld;
import universalelectricity.core.transform.vector.Vector2;
import universalelectricity.core.transform.vector.Vector3;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.ArrayList;
import java.util.List;

/** Base entity class for all entities created by artillect mod
 * 
 * @author Darkguardsman */
public class EntityBase extends EntityCreature implements IVectorWorld
{
    private boolean playerOwned = false;
    
    public EntityBase(World world)
    {
        super(world);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    //--------------------------------
    //--------Update methods----------
    //--------------------------------

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    //--------------------------------
    //--------Attack methods----------
    //--------------------------------

    @Override
    public boolean attackEntityAsMob(Entity target)
    {
        boolean didAttack = super.attackEntityAsMob(target);
        if (didAttack && this.getHeldItem() == null && this.isBurning())
        {
            target.setFire(2 * this.worldObj.difficultySetting.ordinal());
        }
        return didAttack;
    }

    /** Gets all entities within the radius(Cylinder Search)
     *
     * @param clazzes - all entities classes to search by
     * @param range - range x & z(NORTH/EAST/SOUTH/WEST)
     * @param rangeY - range y (UP/DOWN)
     * @return null if clazzes is null, or a list(can be empty) of all entities within the range
     */
    public List<Entity> getAllEntities(Iterable<Class< ? extends Entity>> clazzes, double range, double rangeY)
    {
        if(clazzes != null)
        {
            List entities = world().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x() + range, y() + rangeY, z() + range, x() - range, y() - rangeY, z() - range));
            List<Entity> actual = new ArrayList<Entity>();
            for(Object obj : entities)
            {
                Entity ent = (Entity) obj;
                for (Class<? extends Entity> clazz : clazzes)
                {
                    if(clazz.isAssignableFrom(ent.getClass()))
                    {
                        actual.add(ent);
                        break;
                    }
                }
            }
            return actual;
        }
        return null;
    }


    /** Gets all entities within the radius(Cylinder Search) that are hostile
     *
     * @param range - range x & z(NORTH/EAST/SOUTH/WEST)
     * @param rangeY - range y (UP/DOWN)
     * @return a list(can be empty) of all entities within the range
     */
    public List<Entity> getHostileEntities(double range, double rangeY)
    {
            List entities = world().getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x() + range, y() + rangeY, z() + range, x() - range, y() - rangeY, z() - range));
            List<Entity> actual = new ArrayList<Entity>();
            for(Object obj : entities)
            {
                Entity ent = (Entity) obj;
                if(isHostile(ent))
                {
                    actual.add(ent);
                }
            }
            return actual;
    }

    /** Does this entity consider the other entity hostile
     *
     * @param entity - entity to check against, can't be null
     * @return true if its hostile
     */
    public boolean isHostile(Entity entity)
    {
        if(entity instanceof IMob)
        {
            return true;
        }
        return false;
    }


    //--------------------------------
    //--------Data(Read/Write) methods----------
    //--------------------------------

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("playerOwned", this.isPlayerOwned());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setPlayerOwned(nbt.getBoolean("playerOwned"));
    }

    //--------------------------------
    //--------Owner methods----------
    //--------------------------------

    public boolean isPlayerOwned()
    {
        return this.playerOwned;
    }
    
    public void setPlayerOwned(boolean b)
    {
        this.playerOwned = b;
    }

    //--------------------------------
    //--------Location methods----------
    //--------------------------------

    @Override
    public double z()
    {
        return this.posZ;
    }

    @Override
    public double x()
    {
        return this.posX;
    }

    @Override
    public double y()
    {
        return this.posY;
    }

    public int zi()
    {
        return (int)this.posZ;
    }

    public int xi()
    {
        return (int)this.posX;
    }

    public int yi()
    {
        return (int)this.posY;
    }

    @Override
    public World world()
    {
        return this.worldObj;
    }

    public Vector2 asVector2()
    {
        return new Vector2(x(), z());
    }

    public Vector3 asVector3()
    {
        return new Vector3(x(), y(), z());
    }

    public VectorWorld asVectorWorld()
    {
        return new VectorWorld(world(), x(), y(), z());
    }
    
    //--------------------------------
    //--------Helper methods----------
    //--------------------------------
    
    /** Causes the entity to blow up */
    public Explosion blowUp()
    {
        return blowUp(2);
    }
    
    /** Causes the entity to blow up */
    public Explosion blowUp(double size)
    {
        return blowUp(size, true);
    }
    
    /**
     * Causes the entity to blow up
     * @param doDie - should the entity die 
     */
    public Explosion blowUp(double size, boolean doDie)
    {
        Explosion e = causeExplosion(x(), y(), z(), size);
        if(doDie)
        {
            setDead();
        }
        return e;
    }
    
    /**
     * Creates an explosion causes by this entity
     * @param x - x location
     * @param y - y location
     * @param z - z locaion
     * @param size - radius of the explosion
     */
    public Explosion causeExplosion(double x, double y, double z, double size)
    {
        return this.worldObj.createExplosion(this, x, y, z, (float)(size * 2), this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
    }
}
