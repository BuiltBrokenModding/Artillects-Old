package artillects.content.tool.surveyor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import resonant.lib.utility.LanguageUtility;
import artillects.content.tool.GuiPlacedTool;
import artillects.core.prefab.gui.TextField;

public class GuiSurveyor extends GuiPlacedTool
{
    private TextField color_field;

    public GuiSurveyor(EntityPlayer player, TileSurveyor tileEntity)
    {
        super(player, tileEntity);
    }

    /** Adds the buttons (and other controls) to the screen in question. */
    @Override
    public void initGui()
    {
        super.initGui();
        this.color_field = new TextField(fontRenderer, 110, 67, 45, 12).setLength(5);
        color_field.setText(TileSurveyor.rgb2hex(((TileSurveyor) tile).beamColor));
        add(color_field);
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString("\u00a77" + LanguageUtility.getLocal("gui.field.name"), 30, 6, 4210752);
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.field.color"), 25, 67, 4210752);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        if (button.id == apply_button.id)
        {
            ((TileSurveyor) tile).setColor(color_field.getText());
        }
    }
}
