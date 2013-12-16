package artillects.client.model;

import net.minecraft.client.model.ModelSpider;

/**
 * @author Calclavia
 * 
 */
public class ModelFabricator extends ModelSpider
{
	public void render(float par7)
	{
		this.spiderHead.render(par7);
		this.spiderNeck.render(par7);
		this.spiderBody.render(par7);
		this.spiderLeg1.render(par7);
		this.spiderLeg2.render(par7);
		this.spiderLeg3.render(par7);
		this.spiderLeg4.render(par7);
		this.spiderLeg5.render(par7);
		this.spiderLeg6.render(par7);
		this.spiderLeg7.render(par7);
		this.spiderLeg8.render(par7);
	}
}
