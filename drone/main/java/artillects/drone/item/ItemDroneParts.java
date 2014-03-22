package artillects.drone.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import artillects.drone.Drone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Parts and materials used in the machines.
 * 
 * @author DarkGuardsman
 */
public class ItemDroneParts extends ItemBase
{
	public ItemDroneParts(int id)
	{
		super(id, "droneParts");
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() < Part.values().length)
		{
			return super.getUnlocalizedName(itemStack) + "." + Part.values()[itemStack.getItemDamage()].name;
		}
		return super.getUnlocalizedName(itemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (Part part : Part.values())
		{
			par3List.add(new ItemStack(this, 1, part.ordinal()));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int meta)
	{
		if (meta < Part.values().length)
		{
			return Part.values()[meta].icon;
		}
		return this.itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		super.registerIcons(par1IconRegister);
		for (Part part : Part.values())
		{
			part.icon = par1IconRegister.registerIcon(Drone.PREFIX + "dronepart." + part.name);
		}
	}

	public static enum Part
	{
		GEARS("gears"), CIRCUITS_MELTED_T1("meltedCircuits1"),
		CIRCUITS_MELTED_T2("meltedCircuits2"), CIRCUITS_MELTED_T3("meltedCircuits3"),
		CIRCUITS_T1("circuits1"), CIRCUITS_T2("circuits2"), CIRCUITS_T3("circuits3"),
		METAL_PLATE("metalPlate");

		public final String name;
		public Icon icon;

		private Part(String name)
		{
			this.name = name;
		}

	}
}
