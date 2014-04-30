package artillects.core.entity.mobs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityDeadPlayer extends EntityUndead
{
    public ItemStack[] inv = new ItemStack[36];
    
    public EntityDeadPlayer(World world)
    {
        super(world);

    }

    public EntityDeadPlayer(EntityPlayer player)
    {
        super(player.worldObj);
        //Move over player's xp
        this.experienceValue = player.experienceTotal;
        player.experienceTotal = 0;
        player.experienceLevel = 0;
        //Copy over player's armor
        this.setCurrentItemOrArmor(1, player.inventory.armorItemInSlot(0));
        this.setCurrentItemOrArmor(2, player.inventory.armorItemInSlot(1));
        this.setCurrentItemOrArmor(3, player.inventory.armorItemInSlot(2));
        this.setCurrentItemOrArmor(4, player.inventory.armorItemInSlot(3));
        
        //Copy over inventory
        this.inv = player.inventory.mainInventory;
        player.inventory.mainInventory = new ItemStack[this.inv.length];

    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
    }

}
