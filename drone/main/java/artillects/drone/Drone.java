package artillects.drone;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import artillects.core.Artillects;
import artillects.core.ArtillectsTab;
import artillects.core.Reference;
import artillects.drone.block.BlockHiveComplexCore;
import artillects.drone.block.BlockHiveLighting;
import artillects.drone.block.BlockHiveWalling;
import artillects.drone.block.BlockSymbol;
import artillects.drone.block.TileEntityHiveComplexCore;
import artillects.drone.block.lightbridge.BlockLightbridge;
import artillects.drone.block.lightbridge.BlockLightbridgeCore;
import artillects.drone.block.lightbridge.BlockLightbridgeFrame;
import artillects.drone.block.lightbridge.TileLightbridge;
import artillects.drone.block.lightbridge.TileLightbridgeCore;
import artillects.drone.block.teleporter.BlockGlyph;
import artillects.drone.block.teleporter.BlockTeleporterAnchor;
import artillects.drone.block.teleporter.TileEntityTeleporterAnchor;
import artillects.drone.commands.CommandTool;
import artillects.drone.hive.EnumArtillectEntity;
import artillects.drone.hive.HiveComplexManager;
import artillects.drone.hive.worldgen.HiveComplexGenerator;
import artillects.drone.item.ItemArtillectSpawner;
import artillects.drone.item.ItemBuildingGenerator;
import artillects.drone.item.ItemDroneParts;
import artillects.drone.item.ItemDroneParts.Part;
import artillects.drone.item.ItemSchematicCreator;
import artillects.drone.item.weapons.laser.ItemLaserHeavy;
import artillects.drone.item.weapons.laser.ItemLaserPistol;
import artillects.drone.item.weapons.laser.ItemLaserRifle;
import artillects.drone.item.weapons.laser.ItemLaserSniper;
import artillects.drone.item.weapons.plasma.ItemPlasmaLight;
import artillects.drone.item.weapons.plasma.ItemPlasmaPistol;
import artillects.drone.item.weapons.plasma.ItemPlasmaRifle;
import artillects.drone.item.weapons.plasma.ItemPlasmaSniper;
import calclavia.lib.content.ContentRegistry;
import calclavia.lib.content.IDManager;
import calclavia.lib.network.PacketEntity;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.item.ItemBlockMetadata;
import calclavia.lib.utility.LanguageUtility;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Drone.MOD_ID, name = Drone.NAME, version = Reference.VERSION, useMetadata = true)
@NetworkMod(channels = { Reference.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Drone
{

    // @Mod
    public static final String MOD_ID = "Artillects|Drone";
    public static final String NAME = "Artillects";

    @SidedProxy(clientSide = "artillects.drone.ClientProxy", serverSide = "artillects.drone.CommonProxy")
    public static CommonProxy proxy;

    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "Artillects.cfg"));

    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "de_DE" };

    /** Packet Types */
   

    @Instance(Drone.MOD_ID)
    public static Drone instance;

    @Metadata(Drone.MOD_ID)
    public static ModMetadata meta;

    /** Calclavia Gubins */

    public static Block blockGlyph;
    public static Block blockHiveWalling;
    public static Block blockLight;
    public static Block blockHiveTeleporterNode;
    public static Block blockLightbridgeCore;
    public static Block blockLightbridgeFrame;
    public static Block blockLightbridge;

    public static Block blockSymbol;
    public static Block blockHiveCore;

    public static Item itemArtillectSpawner;
    public static Item itemParts;
    public static Item itemBuilding;
    public static Item itemSchematicCreator;
    public static Item weaponPlasmaLauncher;
    public static Item plasmaBattery;
    public static Item laserRifle, laserSniper, laserPistol, laserHeavy;
	public static Item plasmaRifle, plasmaSniper, plasmaPistol, plasmaHeavy, plasmaLight;
    
    public static boolean enableHiveComplexGenerator = true;
    public static boolean enableHiveChunkLoading = true;
    public static boolean enableHiveCoreChunkLoading = true;
    public static int hiveChunkLoadingRange = 5;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        // Register event handlers
        HiveComplexManager.instance();
        NetworkRegistry.instance().registerGuiHandler(this, Drone.proxy);

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register blocks and tiles
        CONFIGURATION.load();
        // Settings
        enableHiveComplexGenerator = CONFIGURATION.get("HiveComplex", "EnableWorldGen", true).getBoolean(true);
        enableHiveChunkLoading = CONFIGURATION.get("HiveComplex", "EnableGeneralChunkLoading", true, "Allows drone complexs to chunkload areas outside the core chunk").getBoolean(true);
        enableHiveCoreChunkLoading = CONFIGURATION.get("HiveComplex", "EnableCoreChunkLoading", true, "Disabling this will cause a hive complex to unload from the map").getBoolean(true);
        hiveChunkLoadingRange = CONFIGURATION.get("HiveComplex", "EnableCoreChunkLoading", 5, "Range by which the hive will chunk load, will enforce a value of 1").getInt(5);

        // Item & block ids
        itemArtillectSpawner = Artillects.contentRegistry.createItem("it1" + "emDrones", ItemArtillectSpawner.class, true);
        itemParts = Artillects.contentRegistry.createItem("itemDroneParts", ItemDroneParts.class, true);
        itemBuilding = Artillects.contentRegistry.createItem("itemBuildingSpawner", ItemBuildingGenerator.class, true);
        itemSchematicCreator = Artillects.contentRegistry.createItem("itemSchematicTool", ItemSchematicCreator.class, true);

        laserRifle = Artillects.contentRegistry.createItem("laserRifle", ItemLaserRifle.class, false);
		laserSniper = Artillects.contentRegistry.createItem("laserSniper", ItemLaserSniper.class, false);
		laserPistol = Artillects.contentRegistry.createItem("laserPistol", ItemLaserPistol.class, false);
		laserHeavy = Artillects.contentRegistry.createItem("laserHeavy", ItemLaserHeavy.class, false);
		
		plasmaRifle = Artillects.contentRegistry.createItem("plasmaRifle", ItemPlasmaRifle.class, false);
		plasmaPistol = Artillects.contentRegistry.createItem("plasmaPistol", ItemPlasmaPistol.class, false);
		plasmaLight = Artillects.contentRegistry.createItem("plasmaLight", ItemPlasmaLight.class, false);
		plasmaSniper = Artillects.contentRegistry.createItem("plasmaSniper", ItemPlasmaSniper.class, false);
        
        blockSymbol = Artillects.contentRegistry.createBlock(BlockSymbol.class, ItemBlockMetadata.class);
        blockHiveWalling = Artillects.contentRegistry.createBlock(BlockHiveWalling.class, ItemBlockMetadata.class);
        blockLight = Artillects.contentRegistry.createBlock(BlockHiveLighting.class, ItemBlockMetadata.class);
        blockGlyph = Artillects.contentRegistry.createBlock(BlockGlyph.class, ItemBlockMetadata.class);
        blockHiveTeleporterNode = Artillects.contentRegistry.createBlock(BlockTeleporterAnchor.class);
        blockHiveCore = Artillects.contentRegistry.createBlock(BlockHiveComplexCore.class);
        blockLightbridgeCore = Artillects.contentRegistry.createBlock(BlockLightbridgeCore.class);
        blockLightbridgeFrame = Artillects.contentRegistry.createBlock(BlockLightbridgeFrame.class);
        blockLightbridge = Artillects.contentRegistry.createBlock(BlockLightbridge.class);

        CONFIGURATION.save();

        ArtillectsTab.itemStack = new ItemStack(blockSymbol);

        // Register entities
        for (EnumArtillectEntity artillect : EnumArtillectEntity.values())
        {
            artillect.register();
        }

        GameRegistry.registerTileEntity(TileLightbridgeCore.class, "tileLightbridgeCore");
        GameRegistry.registerTileEntity(TileLightbridge.class, "tileLightbridge");
        GameRegistry.registerTileEntity(TileEntityTeleporterAnchor.class, "tileHiveTeleporterAnchor");
        GameRegistry.registerTileEntity(TileEntityHiveComplexCore.class, "tileHiveComplexCore");
        if (Drone.enableHiveComplexGenerator)
        {
            GameRegistry.registerWorldGenerator(new HiveComplexGenerator());
        }
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        /* Load Artillect Recipes */
        // Worker
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArtillectSpawner, 1, EnumArtillectEntity.WORKER.ordinal()), "G G", "GCG", "G G", 'G', itemParts, 'C', new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal())));

        // Fabriactor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArtillectSpawner, 1, EnumArtillectEntity.FABRICATOR.ordinal()), "GCG", "GGG", "GCG", 'G', itemParts, 'C', new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal())));

        // Demolisher
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArtillectSpawner, 1, EnumArtillectEntity.DEMOLISHER.ordinal()), "C C", "GGG", "G G", 'G', itemParts, 'C', new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal())));

        // Seeker
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArtillectSpawner, 1, EnumArtillectEntity.SEEKER.ordinal()), "G G", "GGG", " C ", 'G', itemParts, 'C', new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal())));

        /* Load Recipe Item Recipes */
        // Metal Plate
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 4, Part.METAL_PLATE.ordinal()), "II ", "II ", 'I', Item.ingotIron));

        // Metal Gear
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 4, Part.GEARS.ordinal()), "G G", " G ", "G G", 'G', Item.ingotGold));

        // Circuit 1
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal()), "III", "IPI", "III", 'P', new ItemStack(itemParts, 1, ItemDroneParts.Part.METAL_PLATE.ordinal()), 'I', Item.ingotGold));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T1.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T1.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T1.ordinal())));
        // Circuit 2
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T2.ordinal()), "III", "IPI", "III", 'P', new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_T1.ordinal()), 'I', Item.ingotGold));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T2.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T2.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T2.ordinal())));
        // Circuit 3
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T3.ordinal()), "III", "IPI", "III", 'P', new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_T2.ordinal()), 'I', new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_T1.ordinal())));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemParts, 1, Part.CIRCUITS_T3.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T3.ordinal()), new ItemStack(itemParts, 1, ItemDroneParts.Part.CIRCUITS_MELTED_T3.ordinal())));

        // Wall 1
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHiveWalling, 1, 0), "PGP", "G G", "PGP", 'P', new ItemStack(itemParts, 1, ItemDroneParts.Part.METAL_PLATE.ordinal()), 'G', new ItemStack(itemParts, 1, ItemDroneParts.Part.GEARS.ordinal())));
        // Wall 2
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHiveWalling, 1, 2), "GPG", "P P", "GPG", 'P', new ItemStack(itemParts, 1, ItemDroneParts.Part.METAL_PLATE.ordinal()), 'G', new ItemStack(itemParts, 1, ItemDroneParts.Part.GEARS.ordinal())));
        proxy.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        serverCommandManager.registerCommand(new CommandTool());
    }

}
