package artillects.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import artillects.Artillects;

/**
 * @author Calclavia
 * 
 */
public class GuiBase extends GuiContainer
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(Artillects.DOMAIN, Artillects.GUI_DIRECTORY + "gui_base.png");

	/**
	 * The X size of the inventory window in pixels.
	 */
	protected int xSize = 176;

	/**
	 * The Y size of the inventory window in pixels.
	 */
	protected int ySize = 166;

	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;

	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;

	protected int containerWidth, containerHeight;

	public GuiBase(Container container)
	{
		super(container);
		this.ySize = 217;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y)
	{
		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;

		this.mc.renderEngine.bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
	}
}
