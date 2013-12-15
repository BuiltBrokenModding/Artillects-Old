package artillects.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.weapon.EntityPlasma;


public class ItemPlasmaLauncher extends ItemBase {

	public ItemPlasmaLauncher() {
		super("plasmaLauncher");
		setTextureName(Artillects.PREFIX + "plasmaLauncher");
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
		ItemWeaponBattery battery = null;
		
		for(ItemStack stack : player.inventory.mainInventory) {
			if(stack != null) {
				if(stack.getItem() == Artillects.plasmaBattery) {
					battery = (ItemWeaponBattery) stack.getItem();
					break;
				}
			}
		}
		
		/*if(!battery.isEmpty()) {
			par2World.spawnEntityInWorld(new EntityPlasma(par2World, player));
			if(!par2World.isRemote) battery.drain(1, true);
		}*/
		return par1ItemStack;
	}
}
