package artillects.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.IArtillect;
import artillects.hive.ArtillectEntityType;
import artillects.hive.ArtillectType;
import artillects.hive.HiveComplex;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Debug tool use to spawn drones in the same way that monster spawn eggs do.
 * 
 * @author Dark */
public class ItemArtillectSpawner extends ItemBase
{
    private long lastVoiceActivation;

    public ItemArtillectSpawner()
    {
        super("artillectSpawner");
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return super.getUnlocalizedName(itemStack) + "." + ArtillectEntityType.values()[itemStack.getItemDamage()].name;
    }

    /** Callback for item usage. If the item does something special on right clicking, he will have
     * one of those. Return True if something happen and false if it don't. This is for ITEMS, not
     * BLOCKS */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {

            if (player.isSneaking())
            {
                if (System.currentTimeMillis() - this.lastVoiceActivation > 20 * 100)
                {
                    this.lastVoiceActivation = System.currentTimeMillis();
                    switch (ArtillectEntityType.values()[stack.getItemDamage()])
                    {
                        case WORKER:
                            world.playSoundEffect(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, Artillects.PREFIX + "voice-introduce-worker", 5F, 1F);
                            break;
                        case FABRICATOR:
                            world.playSoundEffect(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, Artillects.PREFIX + "voice-introduce-fabricator", 5F, 1F);
                            break;
                    }
                }
            }
            return true;
        }
        else
        {
            if (!player.isSneaking())
            {
                int i1 = world.getBlockId(par4, par5, par6);
                par4 += Facing.offsetsXForSide[par7];
                par5 += Facing.offsetsYForSide[par7];
                par6 += Facing.offsetsZForSide[par7];
                double d0 = 0.0D;

                if (par7 == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
                {
                    d0 = 0.5D;
                }

                Entity entity = spawnCreature(player, world, stack.getItemDamage(), par4 + 0.5D, par5 + d0, par6 + 0.5D);

                if (entity != null)
                {
                    if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                    {
                        ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                    }

                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }
                }
            }

            return true;
        }
    }

    /** Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
     * world, entityPlayer */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return stack;
        }
        else if (!player.isSneaking())
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition == null)
            {
                return stack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k))
                    {
                        return stack;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    if (world.getBlockMaterial(i, j, k) == Material.water)
                    {
                        Entity entity = spawnCreature(player, world, stack.getItemDamage(), i, j, k);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                            {
                                ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode)
                            {
                                --stack.stackSize;
                            }
                        }
                    }
                }

                return stack;
            }
        }
        else
        {
            if (System.currentTimeMillis() - this.lastVoiceActivation > 20 * 100)
            {
                this.lastVoiceActivation = System.currentTimeMillis();
                switch (ArtillectEntityType.values()[stack.getItemDamage()])
                {
                    case WORKER:
                        world.playSoundEffect(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, Artillects.PREFIX + "voice-introduce-worker", 5F, 1F);
                        break;
                    case FABRICATOR:
                        world.playSoundEffect(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, Artillects.PREFIX + "voice-introduce-fabricator", 5F, 1F);
                        break;
                }
            }
            return stack;
        }
    }

    /** Spawns the creature specified by the egg's type in the location specified by the last three
     * parameters. Parameters: world, entityID, x, y, z. */
    public static Entity spawnCreature(EntityPlayer player, World world, int id, double x, double y, double z)
    {
        if (id >= ArtillectEntityType.values().length || ArtillectEntityType.values()[id].getNew(world) == null)
        {
            return null;
        }
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = ArtillectEntityType.values()[id].getNew(world);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entityliving = (EntityLivingBase) entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;

                    if (entityliving instanceof EntityCreature)
                    {
                        ((EntityCreature) entityliving).setHomeArea((int) x, (int) y, (int) z, 50);
                    }

                    if (entityliving instanceof IArtillect)
                    {
                        ((IArtillect) entityliving).setOwner(HiveComplex.getPlayerHive());
                        ((IArtillect) entityliving).setType(ArtillectType.NONE);
                    }

                    world.spawnEntityInWorld(entity);
                }
            }

            return entity;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (ArtillectEntityType drone : ArtillectEntityType.values())
        {
            par3List.add(new ItemStack(this, 1, drone.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
    }
}
