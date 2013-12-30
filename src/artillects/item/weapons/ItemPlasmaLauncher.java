package artillects.item.weapons;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import artillects.Artillects;
import artillects.entity.projectile.EntityPlasma;
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
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Press \247lSHIFT\247r\u00a77 for detail");
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			par3List.add("");
			par3List.add("A launcher that shoots balls of \u00a73plasma\u00a77!");
			par3List.add("Highly \u00a78\247leffective\247r\u00a77 against disabling artillects!");
		}
	}
}
