package artillects.item.weapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.weapon.EntityPlasma;
import artillects.item.ItemBase;

public class ItemPlasmaLauncher extends ItemBase
{

	public ItemPlasmaLauncher()
	{
		super("plasmaLauncher");
		setTextureName(Artillects.PREFIX + "plasmaLauncher");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player)
	{

		par2World.spawnEntityInWorld(new EntityPlasma(par2World, player));
		return par1ItemStack;
	}
}
