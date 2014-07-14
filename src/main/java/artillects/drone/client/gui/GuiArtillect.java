package artillects.drone.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import artillects.drone.container.ContainerArtillect;
import artillects.drone.entity.IArtillect;

/**
 * @author Calclavia
 * 
 */
public class GuiArtillect extends GuiBase
{
	private GuiButton switchTaskButton;
	private IArtillect artillect;

	public GuiArtillect(IArtillect worker, EntityPlayer player)
	{
		super(new ContainerArtillect(worker, player));
		this.artillect = worker;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.switchTaskButton = new GuiButton(0, this.width / 2 - 78, this.height / 2 - 70, 80, 20, I18n.getString("gui.switchTask")));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);
		this.fontRenderer.drawString("Artillect", this.xSize / 2 - 15, -18, 4210752);
		this.fontRenderer.drawString("Task: " + this.artillect.getType().name(), 9, 0, 4210752);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == this.switchTaskButton.id)
		{
			this.artillect.setType(this.artillect.getType().toggle((Entity) this.artillect));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(var1, x, y);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.drawSlot(110 + i * 18, 20 + j * 18);
			}
		}
	}
}
