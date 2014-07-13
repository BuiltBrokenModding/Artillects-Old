package artillects.drone;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.item.ItemBlockMetadata;
import artillects.core.Artillects;
import artillects.core.ArtillectsTab;
import artillects.core.Reference;
import artillects.core.building.BuildFile;
import artillects.drone.blocks.BlockHiveComplexCore;
import artillects.drone.blocks.BlockHiveLighting;
import artillects.drone.blocks.BlockHiveWalling;
import artillects.drone.blocks.BlockSymbol;
import artillects.drone.blocks.TileEntityHiveComplexCore;
import artillects.drone.blocks.lightbridge.BlockLightbridge;
import artillects.drone.blocks.lightbridge.BlockLightbridgeCore;
import artillects.drone.blocks.lightbridge.BlockLightbridgeFrame;
import artillects.drone.blocks.lightbridge.TileLightbridge;
import artillects.drone.blocks.lightbridge.TileLightbridgeCore;
import artillects.drone.blocks.teleporter.BlockGlyph;
import artillects.drone.blocks.teleporter.BlockTeleporterAnchor;
import artillects.drone.blocks.teleporter.TileEntityTeleporterAnchor;
import artillects.drone.commands.CommandTool;
import artillects.drone.entity.EnumArtillectEntity;
import artillects.drone.hive.HiveComplexGenerator;
import artillects.drone.hive.HiveComplexManager;
import artillects.drone.items.ItemArtillectSpawner;
import artillects.drone.items.ItemBuildingGenerator;
import artillects.drone.items.ItemDroneParts;
import artillects.drone.items.ItemDroneParts.Part;
import artillects.drone.items.ItemSchematicCreator;
import cpw.mods.fml.common.FMLCommonHandler;
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

@Mod(modid = Drone.MOD_ID, name = Drone.NAME, version = Reference.VERSION, useMetadata = true, dependencies = "required-after:Artillects;")
@NetworkMod(channels = { Reference.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Drone
{

    // @Mod
    public static final String MOD_ID = "Artillects-Drones";
    public static final String NAME = "Artillects-Drones";

    @SidedProxy(clientSide = "artillects.drone.ClientProxy", serverSide = "artillects.drone.CommonProxy")
    public static CommonProxy proxy;

    /** Packet Types */

    @Instance(Drone.MOD_ID)
    public static Drone instance;

    @Metadata(Drone.MOD_ID)
    public static ModMetadata meta;

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
        // Settings
        enableHiveComplexGenerator = Artillects.CONFIG.get("HiveComplex", "EnableWorldGen", true).getBoolean(true);
        enableHiveChunkLoading = Artillects.CONFIG.get("HiveComplex", "EnableGeneralChunkLoading", true, "Allows drone complexs to chunkload areas outside the core chunk").getBoolean(true);
        enableHiveCoreChunkLoading = Artillects.CONFIG.get("HiveComplex", "EnableCoreChunkLoading", true, "Disabling this will cause a hive complex to unload from the map").getBoolean(true);
        hiveChunkLoadingRange = Artillects.CONFIG.get("HiveComplex", "EnableCoreChunkLoading", 5, "Range by which the hive will chunk load, will enforce a value of 1").getInt(5);

        // Item & block ids
        itemArtillectSpawner = Artillects.contentRegistry.createItem(ItemArtillectSpawner.class);
        itemParts = Artillects.contentRegistry.createItem(ItemDroneParts.class);
        itemBuilding = Artillects.contentRegistry.createItem(ItemBuildingGenerator.class);
        itemSchematicCreator = Artillects.contentRegistry.createItem(ItemSchematicCreator.class);

        blockSymbol = Artillects.contentRegistry.createBlock(BlockSymbol.class, ItemBlockMetadata.class);
        blockHiveWalling = Artillects.contentRegistry.createBlock(BlockHiveWalling.class, ItemBlockMetadata.class);
        blockLight = Artillects.contentRegistry.createBlock(BlockHiveLighting.class, ItemBlockMetadata.class);
        blockGlyph = Artillects.contentRegistry.createBlock(BlockGlyph.class, ItemBlockMetadata.class);
        blockHiveTeleporterNode = Artillects.contentRegistry.createBlock(BlockTeleporterAnchor.class);
        blockHiveCore = Artillects.contentRegistry.createBlock(BlockHiveComplexCore.class);
        blockLightbridgeCore = Artillects.contentRegistry.createBlock(BlockLightbridgeCore.class);
        blockLightbridgeFrame = Artillects.contentRegistry.createBlock(BlockLightbridgeFrame.class);
        blockLightbridge = Artillects.contentRegistry.createBlock(BlockLightbridge.class);

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

        BuildFile.registerSaveBlock("wall1", Drone.blockHiveWalling);
        BuildFile.registerSaveBlock("wall2", Drone.blockHiveWalling);
        BuildFile.registerSaveBlock("symbol1", Drone.blockSymbol);
        BuildFile.registerSaveBlock("symbol2", Drone.blockSymbol);
        BuildFile.registerSaveBlock("symbol3", Drone.blockSymbol);
        BuildFile.registerSaveBlock("light", Drone.blockLight);
        BuildFile.registerSaveBlock("core", Drone.blockHiveCore);
        BuildFile.registerSaveBlock("teleporter", Drone.blockHiveTeleporterNode);
        BuildFile.registerSaveBlock("teleporterSymbol", Drone.blockGlyph);
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
