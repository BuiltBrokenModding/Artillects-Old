package com.builtbroken.artillects;

import com.builtbroken.artillects.content.blocks.door.BlockLockedDoor;
import com.builtbroken.artillects.content.blocks.door.ItemLockedDoor;
import com.builtbroken.artillects.content.blocks.teleporter.BlockTeleporterAnchor;
import com.builtbroken.artillects.content.blocks.teleporter.TileEntityTeleporterAnchor;
import com.builtbroken.artillects.content.items.ItemSchematicCreator;
import com.builtbroken.artillects.content.items.med.ItemBleedingTest;
import com.builtbroken.artillects.content.items.med.ItemMedical;
import com.builtbroken.artillects.content.potion.PotionBleeding;
import com.builtbroken.artillects.content.tool.extractor.TileExtractor;
import com.builtbroken.artillects.content.tool.surveyor.TileSurveyor;
import com.builtbroken.artillects.core.commands.CommandTool;
import com.builtbroken.artillects.core.creation.ContentFactory;
import com.builtbroken.artillects.core.region.Faction;
import com.builtbroken.artillects.core.region.Village;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.handler.SaveManager;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.helper.recipe.UniversalRecipe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.util.Arrays;

/** @author DarkGuardsman */
@Mod(modid = Reference.NAME, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:VoltzEngine;")
public class Artillects
{

    @Instance(Reference.NAME)
    public static Artillects INSTANCE;

    @SidedProxy(clientSide = "artillects.client.ClientProxy", serverSide = "artillects.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(Reference.NAME)
    public static ModMetadata metadata;

    public static Configuration CONFIG;

    /** Blocks and Items */
    public static ModManager contentRegistry;
    public static Item itemSchematicCreator;
    public static Item itemLockedDoor;
    public static Item itemBandage;
    public static Item itemBleedTest;

    public static Block blockSurveyor;
    public static Block blockExtractor;
    public static Block blockTeleporter;
    public static Block blockLockedDoor;

    public static int bleedingPotionID = 70;

    public static ContentFactory contentFactory;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) throws NoSuchFieldException
    {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        SaveManager.registerClass("Faction", Faction.class);
        SaveManager.registerClass("Village", Village.class);

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
        itemLockedDoor = contentRegistry.newItem(ItemLockedDoor.class);
        itemBandage = contentRegistry.newItem(ItemMedical.class);
        itemBleedTest = contentRegistry.newItem(ItemBleedingTest.class);

        blockSurveyor = contentRegistry.newBlock(TileSurveyor.class);
        blockExtractor = contentRegistry.newBlock(TileExtractor.class);
        blockTeleporter = contentRegistry.newBlock(BlockTeleporterAnchor.class);
        blockLockedDoor = contentRegistry.newBlock("CustomLockedDoor", new BlockLockedDoor(), ItemBlock.class);

        GameRegistry.registerTileEntity(TileEntityTeleporterAnchor.class, "tileHiveTeleporterAnchor");

        proxy.preInit();
        setModMetadata();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSurveyor), "IGI", "ICI", "I I", 'G', Blocks.glass, 'C', UniversalRecipe.CIRCUIT_T2.get(), 'I', UniversalRecipe.PRIMARY_METAL.get()));
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
    }

    @SubscribeEvent
    public void onHurtEvent(LivingAttackEvent event)
    {
        if(event.source != null)
        {
            Entity ent = event.source.getSourceOfDamage();
            if(ent instanceof EntityLivingBase)
            {
                EntityLivingBase entity = ((EntityLivingBase) ent);
                ItemStack stack = entity.getHeldItem();
                if(stack != null && (stack.getUnlocalizedName().contains("sword") || stack.getUnlocalizedName().contains("ax")))
                {
                    //TODO detect for armor and reduce chance by armor type
                    float chance = 0.13f;
                    if(event.entity.worldObj.difficultySetting == EnumDifficulty.HARD)
                    {
                        chance = 0.24f;
                    }
                    if(Settings.ENABLE_BLEEDING && entity.worldObj.rand.nextFloat() <= chance)
                    {
                        event.entityLiving.addPotionEffect(new PotionEffect(PotionBleeding.BLEEDING.getId(), 6000));
                    }
                }
            }
        }
    }
}
