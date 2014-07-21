package artillects.core.prefab.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import artillects.core.Reference;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import resonant.lib.gui.ContainerDummy;
import resonant.lib.gui.GuiContainerBase;

/** Prefab for all tiles
 * 
 * @author Darkguardsma */
public class GuiTile extends GuiContainerBase
{

    public static final ResourceLocation BLANK_GUI = new ResourceLocation(Reference.DOMAIN, Reference.GUI_DIRECTORY + "gui_empty.png");
    protected ResourceLocation texture;
    
    protected List<TextField> textBoxes;

    public GuiTile(EntityPlayer player, TileEntity tile)
    {
        this(new ContainerDummy(player, tile));
    }

    public GuiTile(Container container)
    {
        super(container);
        textBoxes = new ArrayList<TextField>();
    }

    public void add(Gui object)
    {
        if (object instanceof TextField)
        {
            textBoxes.add((TextField) object);
        }
        else if (object instanceof GuiButton)
        {
            this.buttonList.add(object);
        }
    }

    @Override
    public void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        for (TextField field : textBoxes)
        {
            field.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        for (TextField field : textBoxes)
        {
            field.mouseClicked(x, y, button);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        for (TextField field : textBoxes)
        {
            field.drawTextBox();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture == null ? BLANK_GUI : texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
    }
}
