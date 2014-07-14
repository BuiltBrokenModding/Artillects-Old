package artillects.core.prefab.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class TextField extends GuiTextField
{
    public TextField(FontRenderer fontRender, int xPos, int yPos, int width, int height)
    {
        super(fontRender, xPos, yPos, width, height);
    }

    public TextField setLength(int length)
    {
        super.setMaxStringLength(length);
        return this;
    }

    public int getTextAsInt()
    {
        String text = this.getText();
        if (text != null && !text.isEmpty())
        {
            try
            {
                return Integer.parseInt(text);
            }
            catch (Exception e)
            {

            }
        }
        return 0;
    }
    
    public double getTextAsDouble()
    {
        String text = this.getText();
        if (text != null && !text.isEmpty())
        {
            try
            {
                return Double.parseDouble(text);
            }
            catch (Exception e)
            {

            }
        }
        return 0;
    }
    
    public float getTextAsFloat()
    {
        String text = this.getText();
        if (text != null && !text.isEmpty())
        {
            try
            {
                return Float.parseFloat(text);
            }
            catch (Exception e)
            {

            }
        }
        return 0;
    }

}
