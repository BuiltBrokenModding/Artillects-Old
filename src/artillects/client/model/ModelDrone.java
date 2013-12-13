package artillects.client.model;

import net.minecraft.client.model.ModelBase;
import artillects.client.render.Animation;

public abstract class ModelDrone extends ModelBase
{
	int frame = 0;
	Animation animation;

	public void setFrame(int frame)
	{
		this.frame = frame;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}

	public abstract void render(float f5);
}
