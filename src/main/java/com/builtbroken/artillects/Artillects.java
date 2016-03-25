package com.builtbroken.artillects;

import com.builtbroken.artillects.content.items.ItemSchematicCreator;
import com.builtbroken.artillects.content.teleporter.TileEntityTeleporterAnchor;
import com.builtbroken.artillects.core.commands.CommandTool;
import com.builtbroken.artillects.core.creation.ContentFactory;
import com.builtbroken.artillects.core.faction.Faction;
import com.builtbroken.artillects.core.faction.FactionManager;
import com.builtbroken.artillects.core.faction.land.Land;
import com.builtbroken.artillects.core.integration.UsageManager;
import com.builtbroken.artillects.core.integration.templates.UsageStorage;
import com.builtbroken.artillects.core.integration.vanilla.UsageBrewing;
import com.builtbroken.artillects.core.integration.vanilla.UsageFurnace;
import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;

/** @author DarkGuardsman */
@Mod(modid = Reference.NAME, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:VoltzEngine;")
public class Artillects
{
    public static Logger logger = LogManager.getLogger(Reference.NAME);

    @Instance(Reference.NAME)
    public static Artillects INSTANCE;

    @SidedProxy(clientSide = "com.builtbroken.artillects.client.ClientProxy", serverSide = "com.builtbroken.artillects.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(Reference.NAME)
    public static ModMetadata metadata;

    public static Configuration CONFIG;

    /** Blocks and Items */
    public static ModManager contentRegistry;
    public static Item itemSchematicCreator;

    public static ContentFactory contentFactory;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) throws NoSuchFieldException
    {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        SaveManager.registerClass("Faction", Faction.class);
        SaveManager.registerClass("FactionLand", Land.class);

        //Request content from RE
        Engine.requestOres();

        //Create config 
        CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), Reference.NAME + ".cfg"));

        //load configs
        CONFIG.load();

        //create content registry
        contentRegistry = new ModManager().setPrefix(Reference.PREFIX).setTab(Reference.CREATIVE_TAB);

        contentFactory = new ContentFactory(contentRegistry);
        try
        {
            contentFactory.load();
            contentFactory.createAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        itemSchematicCreator = contentRegistry.newItem(ItemSchematicCreator.class);

        GameRegistry.registerTileEntity(TileEntityTeleporterAnchor.class, "tileHiveTeleporterAnchor");

        proxy.preInit();
        setModMetadata();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityChest.class, new UsageStorage("tile.usage.vanilla.chest", "tile.inventory.vanilla.chest", MathHelper.generateSqeuncedArray(0, 36)));
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityBrewingStand.class, new UsageBrewing());
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityFurnace.class, new UsageFurnace());
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        //save configs
        CONFIG.save();
        proxy.postInit();
    }

    public void setModMetadata()
    {
        metadata.modId = Reference.NAME;
        metadata.name = Reference.NAME;
        metadata.description = LanguageUtility.getLocal("meta.artillects.description");
        metadata.url = "ARTILLECTS-MOD.COM";
        metadata.logoFile = "artillects_logo.png";
        metadata.version = Reference.VERSION + "." + Reference.BUILD_VERSION;
        metadata.authorList = Arrays.asList("DarkCow", "Calclavia", "Archadia");
        metadata.credits = LanguageUtility.getLocal("meta.artillects.credits");
        metadata.autogenerated = false;
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        serverCommandManager.registerCommand(new CommandTool());
        FactionManager.loadFromDisk();
    }

    @EventHandler
    public void serverClosing(FMLServerStoppingEvent event)
    {
        FactionManager.saveData();
    }
}