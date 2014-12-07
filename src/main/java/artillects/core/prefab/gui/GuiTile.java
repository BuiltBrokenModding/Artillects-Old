package artillects.core.prefab.gui;

import artillects.core.Reference;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import resonant.lib.prefab.gui.ContainerDummy;
import resonant.lib.prefab.gui.GuiContainerBase;

import java.util.ArrayList;
import java.util.List;

/** Prefab for all tiles
 * 
 * @author Darkguardsma */
public class GuiTile extends GuiContainerBase
{

    public static final ResourceLocation BLANK_GUI = new ResourceLocation(Reference.DOMAIN, Reference.GUI_DIRECTORY + "gui_empty.png");
    protected ResourceLocation texture;
    
    protected List<GuiTextField> textBoxes;

    public GuiTile(EntityPlayer player, TileEntity tile)
    {
        this(new ContainerDummy(player, tile));
    }

    public GuiTile(Container container)
    {
        super(container);
        textBoxes = new ArrayList<GuiTextField>();
    }

    @SuppressWarnings("unchecked")
    public void add(Gui object)
    {
        if (object instanceof GuiTextField)
        {
            textBoxes.add((GuiTextField) object);
        }
        else if (object instanceof GuiButton)
        {
            buttonList.add(object);
        }
    }

    @Override
    public void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        for (GuiTextField field : textBoxes)
        {
            field.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        for (GuiTextField field : textBoxes)
        {
            field.mouseClicked(x - containerWidth, y - containerHeight, button);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        for (GuiTextField field : textBoxes)
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
