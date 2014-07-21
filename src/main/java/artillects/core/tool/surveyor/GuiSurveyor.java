package artillects.core.tool.surveyor;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import resonant.lib.gui.ContainerDummy;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.utility.LanguageUtility;
import artillects.core.Reference;
import artillects.core.prefab.gui.NumericField;
import artillects.core.prefab.gui.NumericField.NumericType;
import artillects.core.tool.GuiPlacedTool;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiSurveyor extends GuiPlacedTool
{
    private NumericField red_field;
    private NumericField blue_field;
    private NumericField green_field;

    public GuiSurveyor(EntityPlayer player, TileSurveyor tileEntity)
    {
        super(player, tileEntity);
    }

    /** Adds the buttons (and other controls) to the screen in question. */
    @Override
    public void initGui()
    {
        super.initGui();

        this.red_field = (NumericField) new NumericField(fontRenderer, NumericType.INT, 110, 67, 45, 12).setLength(5);
        this.blue_field = (NumericField) new NumericField(fontRenderer, NumericType.INT, 110, 82, 45, 12).setLength(5);

    }

    /** Call this method from you GuiScreen to process the keys into textbox. */
    @Override
    public void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        this.red_field.textboxKeyTyped(par1, par2);
        this.blue_field.textboxKeyTyped(par1, par2);
        this.green_field.textboxKeyTyped(par1, par2);

    }

    /** Args: x, y, buttonClicked */
    @Override
    public void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.red_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.blue_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.green_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("\u00a77" + LanguageUtility.getLocal("gui.surveyor.name"), 30, 6, 4210752);

        this.red_field.drawTextBox();
        this.blue_field.drawTextBox();
        this.green_field.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            ((TileSurveyor) tile).setColor(new Color(red_field.getTextAsInt(), green_field.getTextAsInt(), blue_field.getTextAsInt()));
        }
    }
}
