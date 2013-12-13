package artillects.item;

import net.minecraft.item.ItemStack;

/** Parts and materials used in the machines.
 * 
 * @author DarkGuardsman */
public class ItemParts extends ItemBase
{
    public ItemParts()
    {
        super("DroneParts");
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "item." + parts.values()[itemStack.getItemDamage()].name;
    }

    public static enum parts
    {
        GEARS("gears"),
        MELTED_CICUITS("meltedCircuits"),
        T2_MELTED_CICUITS("meltedCircuits2"),
        T3_MELTED_CICUITS("meltedCircuits3"),
        CICUITS("circuits"),
        T2_CICUITS("circuits2"),
        T3_CICUITS("circuits3");

        String name;

        private parts(String name)
        {
            this.name = name;
        }

    }
}
