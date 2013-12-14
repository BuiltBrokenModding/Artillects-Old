package artillects.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;


public class ItemPlasmaLauncher extends ItemBase {

	public ItemPlasmaLauncher() {
		super("plasmaLauncher");
		setTextureName(Artillects.PREFIX + "plasmaLauncher");
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
		par2World.spawnEntityInWorld(new EntityPlasma(par2World, player));
		return par1ItemStack;
	}
}
