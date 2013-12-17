package artillects.client;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import artillects.Artillects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler
{
    public static class SoundFile
    {
        final String name;
        final int versions;

        public SoundFile(String name)
        {
            this(name, 1);
        }

        public SoundFile(String name, int versions)
        {
            this.name = name;
            this.versions = versions;
        }
    }

    public static final SoundHandler INSTANCE = new SoundHandler();

    public static final SoundFile[] SOUND_FILES = { new SoundFile("voice-introduce-worker"), new SoundFile("voice-introduce-fabricator"), new SoundFile("voice-introduce-teleporter"), new SoundFile("voice-doNotTouch", 4), new SoundFile("voice-firstSight", 4), new SoundFile("voice-kill", 3), new SoundFile("voice-leave", 6), new SoundFile("voice-lost", 3), new SoundFile("voice-playerDetection", 5), new SoundFile("voice-playerHide", 5), new SoundFile("voice-random", 8), new SoundFile("voice-target", 5), new SoundFile("voice-welcome", 6) };

    @ForgeSubscribe
    public void loadSoundEvents(SoundLoadEvent event)
    {
        for (int i = 0; i < SOUND_FILES.length; i++)
        {
            System.out.println("Loading sound file " + SOUND_FILES[i].name);
            if (SOUND_FILES[i].versions == 1)
            {
                event.manager.addSound(Artillects.PREFIX + SOUND_FILES[i] + ".ogg");
            }
            else
            {
                for (int variation = 1; variation < SOUND_FILES[i].versions; variation++)
                {
                    event.manager.addSound(Artillects.PREFIX + SOUND_FILES[i] + variation + ".ogg");
                }

            }
        }
    }
}