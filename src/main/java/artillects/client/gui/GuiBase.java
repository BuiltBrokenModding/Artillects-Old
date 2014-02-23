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
	public static final ResourceLocation COMPONENT_TEXTURE = new ResourceLocation(Artillects.DOMAIN, Artillects.GUI_DIRECTORY + "gui_components.png");

	/**
	 * The X size of the inventory window in pixels.
	 */
	protected int xSize = 176;

	/**
	 * The Y size of the inventory window in pixels.
	 */
	protected int ySize = 166;

	protected int containerWidth, containerHeight;

	public GuiBase(Container container)
	{
		super(container);
		this.ySize = 216;
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

	protected void drawSlot(int x, int y)
	{
		this.mc.renderEngine.bindTexture(COMPONENT_TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);
	}

}
