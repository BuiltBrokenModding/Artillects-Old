package artillects.core;

import net.minecraft.creativetab.CreativeTabs;
import artillects.core.Reference;
import artillects.core.Settings;
import calclavia.lib.content.ContentRegistry;
import calclavia.lib.network.PacketEntity;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.PacketTile;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

/** @author DarkGuardsman */
@Mod(modid = Artillects.ID, name = Artillects.NAME, version = Reference.VERSION, dependencies = "required-after:CalclaviaCore;")
@NetworkMod(channels = Reference.CHANNEL, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Artillects
{
    /** Mod Information */
    public static final String ID = "ArtillectsCore";
    public static final String NAME = Reference.NAME;

    @Instance(ID)
    public static Artillects INSTANCE;

    @SidedProxy(clientSide = "artillects.core.ClientProxy", serverSide = "artillects.core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(ID)
    public static ModMetadata metadata;

    /** Packets */
    public static final PacketTile PACKET_TILE = new PacketTile(Reference.CHANNEL);
    public static final PacketEntity PACKET_ENTITY = new PacketEntity(Reference.CHANNEL);

    /** Blocks and Items */
    public static final ContentRegistry contentRegistry = new ContentRegistry(Settings.CONFIGURATION, Settings.idManager, ID).setPrefix(Reference.PREFIX).setTab(CreativeTabs.tabMisc);

    
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
        Settings.CONFIGURATION.load();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        Settings.CONFIGURATION.save();
        proxy.postInit();
    }
}
