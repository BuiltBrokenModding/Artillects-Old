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
import resonant.lib.recipe.UniversalRecipe;
import artillects.core.Artillects;
import artillects.core.ArtillectsTab;
import artillects.core.Reference;
import artillects.core.building.BuildFile;
import artillects.drone.blocks.BlockHiveComplexCore;
import artillects.drone.blocks.BlockHiveLighting;
import artillects.drone.blocks.BlockHiveWalling;
import artillects.drone.blocks.BlockSymbol;
import artillects.drone.blocks.TileEntityHiveComplexCore;
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

        ArtillectsTab.itemStack = new ItemStack(blockSymbol);

        // Register entities
        for (EnumArtillectEntity artillect : EnumArtillectEntity.values())
        {
            artillect.register();
        }

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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHiveWalling, 16, 0), "CBC", "BCB", "CBC", 'C', UniversalRecipe.PRIMARY_METAL.get(), 'B', Block.stone ));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHiveWalling, 1, 0), "CB", 'C', blockHiveWalling, 'B', Item.glowstone));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHiveTeleporterNode), "CBC", "BEB", "CBC", 'E', Item.eyeOfEnder, 'C', UniversalRecipe.CIRCUIT_T2.get(), 'B', Block.blockIron ));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGlyph, 1, 0), "BC", "QQ", 'B', UniversalRecipe.PRIMARY_METAL.get(), 'Q', Item.netherQuartz, 'C', UniversalRecipe.CIRCUIT_T1.get()));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGlyph, 1, 1), "B", 'B', new ItemStack(blockSymbol, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGlyph, 1, 2), "B", 'B', new ItemStack(blockSymbol, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGlyph, 1, 3), "B", 'B', new ItemStack(blockSymbol, 1, 2)));
        
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
