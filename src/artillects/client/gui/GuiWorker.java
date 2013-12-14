package artillects.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import artillects.container.ContainerWorker;
import artillects.entity.EntityWorker;

/**
 * @author Calclavia
 * 
 */
public class GuiWorker extends GuiBase
{
	public GuiWorker(EntityWorker worker, EntityPlayer player)
	{
		super(new ContainerWorker(worker, player));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);
		this.fontRenderer.drawString("Worker", this.xSize / 2 - 15, - 18, 4210752);
		this.fontRenderer.drawString("Task: None", 9, 0, 4210752);
		this.fontRenderer.drawString("Energy: 0 J", 9, 15, 4210752);
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
