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
		if (!player.capabilities.isCreativeMode) {
			--par1ItemStack.stackSize;
		}
		par2World.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!par2World.isRemote) {
			par2World.spawnEntityInWorld(new EntityPlasma(par2World, player.posX, player.posY, player.posZ));
		}
		return par1ItemStack;
	}
}
