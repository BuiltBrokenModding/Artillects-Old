package artillects.core.creation;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import org.w3c.dom.Element;

public class RecipeFactory
{
    public static HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();

    public static IRecipe create(Element element)
    {
        IRecipe recipe = null;
        if (element.hasAttribute("type"))
        {
            if (element.getAttribute("type").equalsIgnoreCase("shapless"))
            {
                recipe = createShapeless(element);
            }
            else if (element.getAttribute("type").equalsIgnoreCase("shaped"))
            {
                recipe = createShaped(element);
            }
            else if (element.getAttribute("type").equalsIgnoreCase("furnace"))
            {
                createFurnace(element);
            }
        }
        return recipe;
    }

    public static IRecipe createShapeless(Element element)
    {
        IRecipe recipe = null;

        return recipe;
    }

    public static IRecipe createShaped(Element element)
    {
        IRecipe recipe = null;

        return recipe;
    }

    public static void createFurnace(Element element)
    {

    }
}
