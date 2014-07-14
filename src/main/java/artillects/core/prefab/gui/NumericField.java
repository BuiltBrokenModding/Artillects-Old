package artillects.core.prefab.gui;

import java.util.ArrayList;
import java.util.List;

import scala.Char;
import net.minecraft.client.gui.FontRenderer;

@SuppressWarnings("unchecked")
public class NumericField extends TextField
{
    NumericType type = NumericType.INT;
    static List charsA = new ArrayList();
    static List charsB = new ArrayList();

    static
    {
        
        char[] s = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-' };
        for (char c : s)
        {
            charsA.add(c);
            charsB.add(c);
        }
        charsB.add('.');
    }

    public NumericField(FontRenderer fontRender, NumericType type, int xPos, int yPos, int width, int height)
    {
        super(fontRender, xPos, yPos, width, height);
        this.type = type;
    }

    public void writeText(String str)
    {
        try
        {
            char[] chars = str.toCharArray();
            for (char c : chars)
            {
                if (type == NumericType.INT)
                {
                    if (!charsA.contains(c))
                    {
                        return;
                    }
                }
                else if (!charsB.contains(c))
                {
                    return;
                }
            }
            super.writeText(str);
        }
        catch (Exception e)
        {

        }
    }

    public static enum NumericType
    {
        INT,
        DOUBLE,
        FLOAT;
    }

}
