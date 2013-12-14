package artillects.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import artillects.Vector3;

/**
 * @author Calclavia
 * 
 */
public class FxLaser extends EntityFX
{
	double movX = 0.0D;
	double movY = 0.0D;
	double movZ = 0.0D;

	private float length = 0.0F;
	private float rotYaw = 0.0F;
	private float rotPitch = 0.0F;
	private float prevYaw = 0.0F;
	private float prevPitch = 0.0F;
	private Vector3 target = new Vector3();
	private float endModifier = 1.0F;
	private boolean reverse = false;
	private boolean pulse = true;
	private int rotationSpeed = 20;
	private float prevSize = 0.0F;

	public FxLaser(World par1World, Vector3 position, Vector3 target, float r, float g, float b, int age)
	{
		super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
		this.setRGB(r, g, b);
		this.setSize(0.02F, 0.02F);
		this.noClip = true;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.target = target;
		float xd = (float) (this.posX - this.target.x);
		float yd = (float) (this.posY - this.target.y);
		float zd = (float) (this.posZ - this.target.z);
		this.length = (float) new Vector3(this).distance(this.target);
		double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
		this.rotYaw = ((float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D));
		this.rotPitch = ((float) (Math.atan2(yd, var7) * 180.0D / 3.141592653589793D));
		this.prevYaw = this.rotYaw;
		this.prevPitch = this.rotPitch;
	}

	public void setRGB(float r, float g, float b)
	{
		this.particleRed = r;
		this.particleGreen = g;
		this.particleBlue = b;
	}
}
