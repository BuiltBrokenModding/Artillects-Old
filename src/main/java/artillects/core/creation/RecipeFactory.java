package artillects.core.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import artillects.core.creation.content.BlockProduct;
import artillects.core.creation.content.ItemProduct;
import artillects.core.creation.content.Product;

public class RecipeFactory
{
    public static HashMap<String, Object> items = new HashMap<String, Object>();
    public static HashMap<String, Object> blocks = new HashMap<String, Object>();

    public static IRecipe create(Product product, Element element)
    {
        ItemStack stack = null;
        Item item = product instanceof ItemProduct ? ((ItemProduct) product).getProduct() : null;
        Block block = product instanceof BlockProduct ? ((BlockProduct) product).getProduct() : null;
        if (element.hasAttribute("result"))
        {
            String res = element.getAttribute("result");
            int stackSize = element.hasAttribute("stackSize") ? Integer.parseInt(element.getAttribute("stackSize")) : 1;
            if (res.startsWith("meta"))
            {
                if (item != null)
                    stack = new ItemStack(item, stackSize, Integer.parseInt(res.replace("meta", "")));
                else
                    stack = new ItemStack(block, stackSize, Integer.parseInt(res.replace("meta", "")));
            }
        }
        if (stack != null)
        {
            return create(stack, element);
        }
        return null;
    }

    public static IRecipe create(ItemStack result, Element element)
    {
        IRecipe recipe = null;
        if (element.hasAttribute("type"))
        {
            if (element.getAttribute("type").equalsIgnoreCase("shapless"))
            {
                recipe = createShapeless(result, element);
            }
            else if (element.getAttribute("type").equalsIgnoreCase("shaped"))
            {
                recipe = createShaped(result, element);
            }
            else if (element.getAttribute("type").equalsIgnoreCase("furnace"))
            {
                createFurnace(result, element);
            }
        }
        return recipe;
    }

    public static ShapelessOreRecipe createShapeless(ItemStack result, Element element)
    {
        List<Object> objects = new ArrayList<Object>();
        NodeList list = element.getElementsByTagName("item");
        for (int i = 0; i < list.getLength(); i++)
        {
            Object object = getPart((Element) list.item(i));
            if (object != null)
            {
                objects.add(object);
            }
        }
        return new ShapelessOreRecipe(result, objects.toArray());
    }

    public static ShapedOreRecipe createShaped(ItemStack result, Element element)
    {
        HashMap<Character, Object> parts = new HashMap<Character, Object>();
        String r = element.getAttribute("recipe");
        String[] rLines = r.split(",");
        NodeList list = element.getElementsByTagName("item");
        for (int i = 0; i < list.getLength(); i++)
        {
            Element e = (Element) list.item(i);
            char c = e.getAttribute("c").toCharArray()[0];
            Object object = getPart(e);
            if (object != null)
            {
                parts.put(c, object);
            }
        }
        Object[] objects = new Object[rLines.length + (parts.size() * 2)];
        int s = 0;
        for (int i = 0; i < rLines.length; i++)
        {
            objects[s] = rLines[i];
            s++;
        }
        for (Entry<Character, Object> entry : parts.entrySet())
        {
            objects[s] = entry.getKey();
            objects[s + 1] = entry.getValue();
            s += 2;
        }
        return new ShapedOreRecipe(result, objects);
    }

    public static void createFurnace(ItemStack result, Element element)
    {
        NodeList list = element.getElementsByTagName("item");
        float xp = element.hasAttribute("xp") ? Float.parseFloat(element.getAttribute("xp")) : 0f;
        for (int i = 0; i < list.getLength(); i++)
        {
            Element e = (Element) list.item(i);
            Object object = getPart(e);
            if (object instanceof Item)
            {
                FurnaceRecipes.smelting().addSmelting(((Item) object).itemID, result, xp);
            }
            else if (object instanceof Block)
            {
                FurnaceRecipes.smelting().addSmelting(((Block) object).blockID, result, xp);
            }
            else if (object instanceof ItemStack)
            {
                FurnaceRecipes.smelting().addSmelting(((ItemStack) object).itemID, ((ItemStack) object).getItemDamage(), result, xp);
            }
            else if (object instanceof String)
            {
                List<ItemStack> stacks = OreDictionary.getOres(((String) object));
                for (ItemStack stack : stacks)
                {
                    FurnaceRecipes.smelting().addSmelting(stack.itemID, stack.getItemDamage(), result, xp);
                }
            }
        }
    }

    public static Object getPart(Element e)
    {
        if (e.hasAttribute("item"))
        {
            String[] s = e.getAttribute("item").split(":");
            if (items.containsKey(e.getAttribute("item")))
            {
                return items.get(e.getAttribute("item"));
            }
            else
            {
                int meta = -1;
                if (s.length > 1)
                {
                    meta = Integer.parseInt(s[1]);
                }
                Item item = getItem(s[0]);
                if (item != null)
                {
                    if (meta != -1)
                    {
                        items.put(e.getAttribute("item"), item);
                        return item;
                    }
                    else
                    {
                        ItemStack stack = new ItemStack(item, 1, meta);
                        items.put(e.getAttribute("item"), stack);
                        return stack;
                    }
                }

            }
        }
        else if (e.hasAttribute("block"))
        {
            String[] s = e.getAttribute("block").split(":");
            if (blocks.containsKey(e.getAttribute("block")))
            {
                return blocks.get(e.getAttribute("block"));
            }
            else
            {
                int meta = -1;
                if (s.length > 1)
                {
                    meta = Integer.parseInt(s[1]);
                }
                Block block = getBlock(s[0]);
                if (block != null)
                {
                    if (meta != -1)
                    {
                        items.put(e.getAttribute("block"), block);
                        return block;
                    }
                    else
                    {
                        ItemStack stack = new ItemStack(block, 1, meta);
                        items.put(e.getAttribute("block"), stack);
                        return stack;
                    }
                }

            }
        }
        else if (e.hasAttribute("ore"))
        {
            return e.getAttribute("ore");
        }
        return null;
    }

    public static Block getBlock(String name)
    {
        for (Block block : Block.blocksList)
        {
            if (block == null)
            {
                continue;
            }

            String blockName = block.getUnlocalizedName().replace("item.", "").replace(".name", "").replace("minecraft.", "");

            if (blockName.equalsIgnoreCase(name))
            {
                return block;
            }
        }
        return null;
    }

    public static Item getItem(String name)
    {
        for (Item item : Item.itemsList)
        {
            if (item == null)
            {
                continue;
            }

            String itemName = item.getUnlocalizedName().replace("item.", "").replace(".name", "").replace("minecraft.", "");

            if (itemName.equalsIgnoreCase(name))
            {
                return item;
            }
        }
        return null;
    }
}
