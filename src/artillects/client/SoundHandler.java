package artillects.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import artillects.Artillects;

@SideOnly(Side.CLIENT)
public class SoundHandler
{
	public static final SoundHandler INSTANCE = new SoundHandler();

	public static final String[] SOUND_FILES = { "voice-agressionDetected.ogg", "voice-intruderAlert.ogg", "voice-pleaseDisarmYourself.ogg", "voice-theHiveIsUnderAttack.ogg" };

	@ForgeSubscribe
	public void loadSoundEvents(SoundLoadEvent event)
	{
		for (int i = 0; i < SOUND_FILES.length; i++)
		{
			event.manager.addSound(Artillects.PREFIX + SOUND_FILES[i]);
		}
	}
}