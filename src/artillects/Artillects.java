package artillects;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import artillects.block.BlockBaseDecor;
import artillects.block.BlockGravitySlow;
import artillects.block.teleporter.BlockHiveTeleporterNode;
import artillects.block.teleporter.BlockHiveTeleporterShape;
import artillects.block.teleporter.tile.TileHiveTNode;
import artillects.block.teleporter.tile.TileHiveTeleporterShape;
import artillects.block.teleporter.util.Shape;
import artillects.entity.ArtillectType;
import artillects.hive.Hive;
import artillects.item.ItemArtillectSpawner;
import artillects.item.ItemBuildingGenerator;
import artillects.item.ItemParts;
import artillects.item.ItemParts.Part;
import artillects.item.ItemPlasmaLauncher;
import artillects.item.ItemSchematicCreator;
import artillects.item.ItemWeaponBattery;
import artillects.network.PacketEntity;
import artillects.network.PacketHandler;
import artillects.network.PacketTile;
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
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Artillects.ID, name = Artillects.NAME, version = Artillects.VERSION, useMetadata = true)
@NetworkMod(channels = { Artillects.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class Artillects
{
    @Instance
    public static Artillects INSTANCE;

    // @Mod Prerequisites
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVIS_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;

    // @Mod
    public static final String ID = "Artillects";
    public static final String NAME = "Artillects";

    @SidedProxy(clientSide = "artillects.client.ClientProxy", serverSide = "artillects.CommonProxy")
    public static CommonProxy proxy;

    public static final String CHANNEL = "Artillects";

    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "Dark/Artillects.cfg"));

    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US" };

    // Domain and prefix
    public static final String DOMAIN = "artillects";
    public static final String PREFIX = DOMAIN + ":";

    // File paths
    public static final String RESOURCE_DIRECTORY_NO_SLASH = "assets/" + DOMAIN + "/";
    public static final String RESOURCE_DIRECTORY = "/" + RESOURCE_DIRECTORY_NO_SLASH;
    public static final String LANGUAGE_PATH = RESOURCE_DIRECTORY + "lang/";
    public static final String SOUND_PATH = RESOURCE_DIRECTORY + "audio/";

    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String BLOCK_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
    public static final String ITEM_DIRECTORY = TEXTURE_DIRECTORY + "items/";
    public static final String MODEL_DIRECTORY = TEXTURE_DIRECTORY + "models/";
    public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";

    /* START IDS */
    public static int BLOCK_ID_PRE = 3856;
    public static int ITEM_ID_PREFIX = 15966;

    /** Packet Types */
    public static final PacketTile PACKET_TILE = new PacketTile();
    public static final PacketEntity PACKET_ENTITY = new PacketEntity();

    @Instance(Artillects.ID)
    private static Artillects instance;

    @Metadata(Artillects.ID)
    public static ModMetadata meta;

    public static Artillects instance()
    {
        if (instance == null)
        {
            instance = new Artillects();
        }
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance();

        // Load meta
        meta.modId = ID;
        meta.name = NAME;
        meta.description = "Alien in nature, it is unknown how these Artillects to exist. What is do know is that they seem to be focused on stripping the planet of its resources...";
        meta.url = "www.universalelectricity.com/artillects";

        meta.logoFile = TEXTURE_DIRECTORY + "Drone_Banner.png";
        meta.version = VERSION;
        meta.authorList = Arrays.asList(new String[] { "Archadia", "DarkGuardsman", "Calclavia", "Hangcow" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;

        // Register event handlers
        TickRegistry.registerScheduledTickHandler(Hive.instance(), Side.SERVER);
        MinecraftForge.EVENT_BUS.register(Hive.instance());
        NetworkRegistry.instance().registerGuiHandler(this, Artillects.proxy);

        proxy.preInit();
    }

    // Moved these here for now ~Archelf
    public static Block blockSymbol1, blockSymbol2, blockSymbol3;
    public static Block blockWall1;
    public static Block blockWall2;
    public static Block blockLight;
    public static Block blockGravity_Slow;
    public static Block blockHiveTeleporterNode;
    public static Block blockTeleporterShape_Square,
    					blockTeleporterShape_Pentagon,
    					blockTeleporterShape_Circle,
    					blockTeleporterShape_Cross;

    public static Item itemArtillectSpawner;
    public static Item itemParts;
    public static Item itemBuilding;
    public static Item itemSchematicCreator;
    public static Item weaponTommygun;
    public static Item plasmaBattery;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register blocks and tiles
        CONFIGURATION.load();
        itemArtillectSpawner = new ItemArtillectSpawner();
        itemParts = new ItemParts();
        itemBuilding = new ItemBuildingGenerator();
        itemSchematicCreator = new ItemSchematicCreator();
        weaponTommygun = new ItemPlasmaLauncher();
        plasmaBattery = new ItemWeaponBattery("plasmaBattery", 20);

        // I've left these non sub-type just in case you need to do anything with them ~Archelf
        blockSymbol1 = new BlockBaseDecor("decorSymbol1");
        blockSymbol2 = new BlockBaseDecor("decorSymbol2");
        blockSymbol3 = new BlockBaseDecor("decorSymbol3");
        blockWall1 = new BlockBaseDecor("decorWall1");
        blockWall2 = new BlockBaseDecor("decorWall2");
        blockLight = new BlockBaseDecor("decorLight").setLightValue(1F);
        blockGravity_Slow = new BlockGravitySlow();
        
        //Teleporter Blocks
        blockHiveTeleporterNode = new BlockHiveTeleporterNode();
        blockTeleporterShape_Square = new BlockHiveTeleporterShape(Shape.SQUARE);
        blockTeleporterShape_Pentagon = new BlockHiveTeleporterShape(Shape.PENTAGON);
        blockTeleporterShape_Circle = new BlockHiveTeleporterShape(Shape.CIRCLE);
        blockTeleporterShape_Cross = new BlockHiveTeleporterShape(Shape.CROSS);
        
        CONFIGURATION.save();

        ArtillectsTab.itemStack = new ItemStack(itemArtillectSpawner);

        System.out.println(NAME + ": Loaded languages: " + loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED));

        // Register entities
        for (ArtillectType artillect : ArtillectType.values())
        {
            artillect.register();
        }

        proxy.init();

        GameRegistry.addRecipe(new ItemStack(plasmaBattery, 1), new Object[] { "X", Character.valueOf('X'), Block.glowStone });

        GameRegistry.registerBlock(blockSymbol1, "blockSymbol1");
        GameRegistry.registerBlock(blockSymbol2, "blockSymbol2");
        GameRegistry.registerBlock(blockSymbol3, "blockSymbol3");
        GameRegistry.registerBlock(blockWall1, "blockWall1");
        GameRegistry.registerBlock(blockWall2, "blockWall2");
        GameRegistry.registerBlock(blockLight, "blockLight");
        GameRegistry.registerBlock(blockGravity_Slow, "blockGravity_Slow");
        GameRegistry.registerBlock(blockHiveTeleporterNode, "blockHiveTeleporterNode");
        GameRegistry.registerBlock(blockTeleporterShape_Square, "blockTeleporterShape_Square");
        GameRegistry.registerBlock(blockTeleporterShape_Pentagon, "blockTeleporterShape_Pentagon");
        GameRegistry.registerBlock(blockTeleporterShape_Circle, "blockTeleporterShape_Circle");
        GameRegistry.registerBlock(blockTeleporterShape_Cross, "blockTeleporterShape_Cross");


        GameRegistry.registerTileEntity(TileHiveTNode.class, "tileHiveTeleporterNode");
        GameRegistry.registerTileEntity(TileHiveTeleporterShape.class, "tileHiveTeleporterShape");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Load crafting
        // Worker
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArtillectSpawner, 1, ArtillectType.WORKER.ordinal()), "G G", "GGG", "G G", 'G', itemParts));

        // Metal Gear
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 4, Part.GEARS.ordinal()), "G G", " G ", "G G", 'G', Item.diamond));

        proxy.postInit();
    }

    /** Loads all the language files for a mod. This supports the loading of "child" language files
     * for sub-languages to be loaded all from one file instead of creating multiple of them. An
     * example of this usage would be different Spanish sub-translations (es_MX, es_YU).
     * 
     * @param languagePath - The path to the mod's language file folder.
     * @param languageSupported - The languages supported. E.g: new String[]{"en_US", "en_AU",
     * "en_UK"}
     * @return The amount of language files loaded successfully. */
    public static int loadLanguages(String languagePath, String[] languageSupported)
    {
        int languages = 0;

        /** Load all languages. */
        for (String language : languageSupported)
        {
            LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", language, false);

            if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
            {
                try
                {
                    String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");

                    for (String child : children)
                    {
                        if (child != "" || child != null)
                        {
                            LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", child, false);
                            languages++;
                        }
                    }
                }
                catch (Exception e)
                {
                    FMLLog.severe("Failed to load a child language file.");
                    e.printStackTrace();
                }
            }

            languages++;
        }

        return languages;
    }

    /** Gets the local text of your translation based on the given key. This will look through your
     * mod's translation file that was previously registered. Make sure you enter the full name
     * 
     * @param key - e.g tile.block.name
     * @return The translated string or the default English translation if none was found. */
    public static String getLocal(String key)
    {
        String text = null;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            text = LanguageRegistry.instance().getStringLocalization(key);
        }

        if (text == null || text == "")
        {
            text = LanguageRegistry.instance().getStringLocalization(key, "en_US");
        }

        return text;
    }

    public static int nextBlockID()
    {
        int id = BLOCK_ID_PRE;

        while (id > 255 && id < (Block.blocksList.length - 1))
        {
            Block block = Block.blocksList[id];
            if (block == null)
            {
                break;
            }
            id++;
        }
        BLOCK_ID_PRE = id + 1;
        return id;
    }

    public static int nextItemID()
    {
        int id = ITEM_ID_PREFIX;

        while (id > 255 && id < (Item.itemsList.length - 1))
        {
            Item item = Item.itemsList[id];
            if (item == null)
            {
                break;
            }
            id++;
        }
        ITEM_ID_PREFIX = id + 1;
        return id;
    }

}
