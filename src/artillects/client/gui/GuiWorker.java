package artillects.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import artillects.container.ContainerWorker;
import artillects.entity.EntityWorker;
import artillects.entity.EntityWorker.EnumWorkerType;

/**
 * @author Calclavia
 * 
 */
public class GuiWorker extends GuiBase
{
	private GuiButton switchTaskButton;
	private EntityWorker worker;

	public GuiWorker(EntityWorker worker, EntityPlayer player)
	{
		super(new ContainerWorker(worker, player));
		this.worker = worker;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.switchTaskButton = new GuiButton(0, this.width / 2 - 80, this.height / 4, 80, 20, I18n.getString("gui.switchTask")));
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);
		this.fontRenderer.drawString("Worker", this.xSize / 2 - 15, -18, 4210752);
		this.fontRenderer.drawString("Task: " + EnumWorkerType.values()[this.worker.getDataWatcher().getWatchableObjectByte(EntityWorker.DATA_TYPE_ID)].name(), 9, 0, 4210752);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == this.switchTaskButton.id)
		{
			byte type = this.worker.getDataWatcher().getWatchableObjectByte(EntityWorker.DATA_TYPE_ID);
			this.worker.getDataWatcher().updateObject(EntityWorker.DATA_TYPE_ID, (byte) (type++ % EnumWorkerType.values().length));
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
