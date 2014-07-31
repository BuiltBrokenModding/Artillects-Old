package artillects.drone;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.item.ItemBlockMetadata;
import resonant.lib.recipe.UniversalRecipe;
import artillects.content.blocks.teleporter.BlockGlyph;
import artillects.content.blocks.teleporter.BlockTeleporterAnchor;
import artillects.content.blocks.teleporter.TileEntityTeleporterAnchor;
import artillects.content.items.ItemArtillectSpawner;
import artillects.content.items.ItemBuildingGenerator;
import artillects.content.items.ItemSchematicCreator;
import artillects.core.Artillects;
import artillects.core.ArtillectsTab;
import artillects.core.Reference;
import artillects.core.building.BuildFile;
import artillects.drone.commands.CommandTool;
import artillects.drone.entity.EnumArtillectEntity;
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

    

    public static Item itemArtillectSpawner;
    public static Item itemBuilding;
    public static Item itemSchematicCreator;
    public static Item weaponPlasmaLauncher;
    public static Item plasmaBattery;

    public static boolean enableHiveComplexGenerator = true;
    public static boolean enableHiveChunkLoading = true;
    public static boolean enableHiveCoreChunkLoading = true;
    public static int hiveChunkLoadingRange = 5;
    public static Block blockGlyph;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        // Register event handlers
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
        itemBuilding = Artillects.contentRegistry.createItem(ItemBuildingGenerator.class);
        itemSchematicCreator = Artillects.contentRegistry.createItem(ItemSchematicCreator.class);

        Artillects.contentRegistry.createBlock("hiveSymbol", Block.class, ItemBlockMetadata.class, null);
        Artillects.contentRegistry.createBlock("hiveWall", Block.class, ItemBlockMetadata.class, null);
        Artillects.contentRegistry.createBlock("hiveLight", Block.class, ItemBlockMetadata.class, null);
        blockGlyph = Artillects.contentRegistry.createBlock(BlockGlyph.class, ItemBlockMetadata.class);
        Artillects.contentRegistry.createBlock(BlockTeleporterAnchor.class);

        ArtillectsTab.itemStack = new ItemStack(Block.anvil);

        // Register entities
        for (EnumArtillectEntity artillect : EnumArtillectEntity.values())
        {
            artillect.register();
        }

        GameRegistry.registerTileEntity(TileEntityTeleporterAnchor.class, "tileHiveTeleporterAnchor");
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
       /** GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockHiveWalling, 16, 0), "CBC", "BCB", "CBC", 'C', UniversalRecipe.PRIMARY_METAL.get(), 'B', Block.stone ));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockHiveWalling, 1, 1), "C", 'C', new ItemStack(Tiles.blockHiveWalling, 1, 0)));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockHiveWalling, 1, 3), "CB", 'C', new ItemStack(Tiles.blockHiveWalling, 1, 0), 'B', Item.glowstone));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockHiveTeleporterNode), "CBC", "BEB", "CBC", 'E', Item.eyeOfEnder, 'C', UniversalRecipe.CIRCUIT_T2.get(), 'B', Block.blockIron ));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockSymbol, 1, 0), "CB", 'C', new ItemStack(Tiles.blockHiveWalling, 1, 0), 'B', new ItemStack(Item.dyePowder, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockSymbol, 1, 1), "BC", 'C', new ItemStack(Tiles.blockHiveWalling, 1, 0), 'B', new ItemStack(Item.dyePowder, 1, 4)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockSymbol, 1, 2), "C", "B", 'C', new ItemStack(Tiles.blockHiveWalling, 1, 0), 'B', new ItemStack(Item.dyePowder, 1, 4)));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSymbol, 1, 3), "B", "C", 'C', new ItemStack(blockHiveWalling, 1, 0), 'B', new ItemStack(Item.dyePowder, 1, 4)));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockGlyph, 1, 0), "BC", "QQ", 'B', UniversalRecipe.PRIMARY_METAL.get(), 'Q', Item.netherQuartz, 'C', UniversalRecipe.CIRCUIT_T1.get()));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockGlyph, 1, 1), "B", 'B', new ItemStack(Tiles.blockGlyph, 1, 0)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockGlyph, 1, 2), "B", 'B', new ItemStack(Tiles.blockGlyph, 1, 1)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockGlyph, 1, 3), "B", 'B', new ItemStack(Tiles.blockGlyph, 1, 2)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Tiles.blockGlyph, 1, 0), "B", 'B', new ItemStack(Tiles.blockGlyph, 1, 3)));
        */ proxy.postInit();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);
        serverCommandManager.registerCommand(new CommandTool());
    }

}
