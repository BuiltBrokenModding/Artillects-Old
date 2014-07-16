package artillects.core.tool.surveyor;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import resonant.lib.gui.ContainerDummy;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.utility.LanguageUtility;
import artillects.core.Reference;
import artillects.core.prefab.gui.NumericField;
import artillects.core.prefab.gui.NumericField.NumericType;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiSurveyor extends GuiContainerBase
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.GUI_DIRECTORY + "gui_empty.png");

    private TileSurveyor tile;
    private NumericField yaw_field;
    private NumericField pitch_field;

    private NumericField red_field;
    private NumericField blue_field;
    private NumericField green_field;

    private int containerWidth;
    private int containerHeight;

    public GuiSurveyor(TileSurveyor tileEntity)
    {
        super(new ContainerDummy());
        this.tile = tileEntity;
        ySize = 166;
    }

    /** Adds the buttons (and other controls) to the screen in question. */
    @Override
    public void initGui()
    {
        super.initGui();
        this.yaw_field = (NumericField) new NumericField(fontRenderer, 110, 37, 45, 12).setLength(10);
        this.pitch_field = (NumericField) new NumericField(fontRenderer, 110, 52, 45, 12).setLength(10);

        this.red_field = (NumericField) new NumericField(fontRenderer, NumericType.INT, 110, 67, 45, 12).setLength(5);
        this.blue_field = (NumericField) new NumericField(fontRenderer, NumericType.INT, 110, 82, 45, 12).setLength(5);
        this.green_field = (NumericField) new NumericField(fontRenderer, NumericType.INT, 110, 97, 45, 12).setLength(5);

        this.yaw_field.setText("" + tile.angle.yaw);
        this.pitch_field.setText("" + tile.angle.pitch);
        this.red_field.setText("" + tile.beamColor.getRed());
        this.blue_field.setText("" + tile.beamColor.getBlue());
        this.green_field.setText("" + tile.beamColor.getGreen());

        this.buttonList.add(new GuiButton(0, this.guiLeft + 40, this.guiTop + 130, 50, 20, LanguageUtility.getLocal("gui.surveyor.apply")));
    }

    /** Call this method from you GuiScreen to process the keys into textbox. */
    @Override
    public void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        this.yaw_field.textboxKeyTyped(par1, par2);
        this.pitch_field.textboxKeyTyped(par1, par2);
        this.red_field.textboxKeyTyped(par1, par2);
        this.blue_field.textboxKeyTyped(par1, par2);
        this.green_field.textboxKeyTyped(par1, par2);
        
    }

    /** Args: x, y, buttonClicked */
    @Override
    public void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.yaw_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.pitch_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.red_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.blue_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
        this.green_field.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.yaw_field.drawTextBox();
        this.pitch_field.drawTextBox();
        this.red_field.drawTextBox();
        this.blue_field.drawTextBox();
        this.green_field.drawTextBox();

        this.fontRenderer.drawString("", 45, 6, 4210752);
        this.fontRenderer.drawString("\u00a77" + LanguageUtility.getLocal("gui.surveyor.name"), 30, 6, 4210752);

        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.surveyor.yaw"), 25, 40, 4210752);
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.surveyor.pitch"), 25, 55, 4210752);
        
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.surveyor.red"), 25, 67, 4210752);
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.surveyor.blue"), 25, 82, 4210752);
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.surveyor.green"), 25, 97, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            tile.setRotation(yaw_field.getTextAsDouble(), pitch_field.getTextAsDouble());
            tile.setColor(new Color(red_field.getTextAsInt(), green_field.getTextAsInt(), blue_field.getTextAsInt()));
        }
    }
}
