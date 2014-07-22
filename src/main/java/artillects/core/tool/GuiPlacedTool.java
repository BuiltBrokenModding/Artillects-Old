package artillects.core.tool;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import resonant.lib.gui.ContainerDummy;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.utility.LanguageUtility;
import artillects.core.Reference;
import artillects.core.prefab.gui.GuiTile;
import artillects.core.prefab.gui.NumericField;
import cpw.mods.fml.client.FMLClientHandler;

/** Gui for any tool that is placed on the ground. Which also include the ability to rotate on the
 * yaw and pitch axis
 * 
 * @author Darkguardsman */
public class GuiPlacedTool extends GuiTile
{
    protected TilePlaceableTool tile;

    protected NumericField yaw_field;
    protected NumericField pitch_field;
    protected GuiButton apply_button;

    public GuiPlacedTool(EntityPlayer player, TilePlaceableTool tileEntity)
    {
        super(player, tileEntity);
        this.tile = tileEntity;
        ySize = 166;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();
        yaw_field = (NumericField) new NumericField(fontRenderer, 110, 37, 45, 12).setLength(10);
        pitch_field = (NumericField) new NumericField(fontRenderer, 110, 52, 45, 12).setLength(10);

        yaw_field.setText("" + tile.angle.yaw);
        pitch_field.setText("" + tile.angle.pitch);

        apply_button = new GuiButton(0, this.guiLeft + 40, this.guiTop + 130, 50, 20, LanguageUtility.getLocal("gui.field.apply"));

        add(yaw_field);
        add(pitch_field);
        add(apply_button);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.field.yaw"), 25, 40, 4210752);
        this.fontRenderer.drawString(LanguageUtility.getLocal("gui.field.pitch"), 25, 55, 4210752);
    }   

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        if (button.id == apply_button.id)
        {
            tile.setRotation(yaw_field.getTextAsDouble(), pitch_field.getTextAsDouble());
        }
    }
}
