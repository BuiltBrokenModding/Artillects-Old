package artillects.core.prefab;

import java.awt.Color;

import resonant.lib.render.RenderUtility;
import universalelectricity.api.vector.Vector3;

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
    
    public void render(Vector3 pos)
    {
        RenderUtility.renderFloatingText(text, pos, color.getRGB());
    }
}