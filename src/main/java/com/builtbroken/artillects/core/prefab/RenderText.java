package com.builtbroken.artillects.core.prefab;

import com.builtbroken.mc.lib.render.RenderUtility;
import com.builtbroken.mc.lib.transform.vector.Pos;

import java.awt.*;

public class RenderText
{
    protected String text;
    protected Color color;
    
    public RenderText(String text, Color color)
    {
        this.text = text;
        this.color = color;
    }
    
    public String getText()
    {
        return text;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public void render(Pos pos)
    {
        RenderUtility.renderFloatingText(text, pos, color.getRGB());
    }
}
