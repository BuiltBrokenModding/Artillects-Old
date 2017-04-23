package com.builtbroken.artillects;

import com.builtbroken.artillects.content.items.ItemSchematicCreator;
import com.builtbroken.artillects.content.npc.EntityCombatTest;
import com.builtbroken.artillects.content.npc.EntityWorkerTest;
import com.builtbroken.artillects.content.npc.ItemSpawnTool;
import com.builtbroken.artillects.content.teleporter.TileEntityTeleporterAnchor;
import com.builtbroken.artillects.core.commands.CommandTool;
import com.builtbroken.artillects.core.creation.ContentFactory;
import com.builtbroken.artillects.core.entity.npc.EntityNpc;
import com.builtbroken.artillects.core.entity.profession.combat.ProfessionProviderGuard;
import com.builtbroken.artillects.core.faction.Faction;
import com.builtbroken.artillects.core.faction.FactionManager;
import com.builtbroken.artillects.core.faction.events.FactionEventHandler;
import com.builtbroken.artillects.core.faction.land.Land;
import com.builtbroken.artillects.core.integration.UsageManager;
import com.builtbroken.artillects.core.integration.templates.UsageStorage;
import com.builtbroken.artillects.core.integration.vanilla.UsageBrewing;
import com.builtbroken.artillects.core.integration.vanilla.UsageFurnace;
import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.registry.ModManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
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

/** @author DarkGuardsman */
@Mod(modid = Reference.NAME, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:voltzengine;")
public class Artillects
{
    public static Logger logger = LogManager.getLogger(Reference.NAME);

    @Instance(Reference.NAME)
    public static Artillects INSTANCE;

    @SidedProxy(clientSide = "com.builtbroken.artillects.client.ClientProxy", serverSide = "com.builtbroken.artillects.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration CONFIG;

    /** Blocks and Items */
    public static ModManager contentRegistry;
    public static Item itemSchematicCreator;
    public static Item itemSpawnTool;

    public static ContentFactory contentFactory;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) throws NoSuchFieldException
    {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        SaveManager.registerClass("Faction", Faction.class);
        SaveManager.registerClass("FactionLand", Land.class);
        MinecraftForge.EVENT_BUS.register(new FactionEventHandler());

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
        itemSpawnTool = contentRegistry.newItem("ArtillectSpawnTool", ItemSpawnTool.class);

        GameRegistry.registerTileEntity(TileEntityTeleporterAnchor.class, "tileHiveTeleporterAnchor");


        EntityRegistry.registerGlobalEntityID(EntityCombatTest.class, "ArtillectsCombatTest", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityCombatTest.class, "ArtillectsCombatTest", 46, this, 500, 1, true);

        EntityRegistry.registerGlobalEntityID(EntityWorkerTest.class, "ArtillectsWorkerTest", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityWorkerTest.class, "ArtillectsWorkerTest", 47, this, 500, 1, true);

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityChest.class, new UsageStorage("tile.usage.vanilla.chest", "tile.inventory.vanilla.chest", MathHelper.generateSqeuncedArray(0, 36)));
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityBrewingStand.class, new UsageBrewing());
        UsageManager.GLOBAL_INSTANCE.registerUsage(TileEntityFurnace.class, new UsageFurnace());

        EntityNpc.registeredProfessions.put("guard", new ProfessionProviderGuard());
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        //save configs
        CONFIG.save();
        proxy.postInit();
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
