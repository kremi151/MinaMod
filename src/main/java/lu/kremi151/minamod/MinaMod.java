package lu.kremi151.minamod;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;

import lu.kremi151.minamod.annotations.MinaPermission;
import lu.kremi151.minamod.api.MinaModAPI;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.block.tileentity.TileEntityCable;
import lu.kremi151.minamod.block.tileentity.TileEntityCampfire;
import lu.kremi151.minamod.block.tileentity.TileEntityCollector;
import lu.kremi151.minamod.block.tileentity.TileEntityElevatorControl;
import lu.kremi151.minamod.block.tileentity.TileEntityEnergySource;
import lu.kremi151.minamod.block.tileentity.TileEntityGiftBox;
import lu.kremi151.minamod.block.tileentity.TileEntityHerbCrop;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.block.tileentity.TileEntityLock;
import lu.kremi151.minamod.block.tileentity.TileEntityPlate;
import lu.kremi151.minamod.block.tileentity.TileEntitySieve;
import lu.kremi151.minamod.capabilities.MinaCapabilities;
import lu.kremi151.minamod.commands.CommandMinaBase;
import lu.kremi151.minamod.entity.EntityBee;
import lu.kremi151.minamod.entity.EntityFish;
import lu.kremi151.minamod.entity.EntityFrostBall;
import lu.kremi151.minamod.entity.EntityIceGolhem;
import lu.kremi151.minamod.entity.EntityIceSentinel;
import lu.kremi151.minamod.entity.EntityPenguin;
import lu.kremi151.minamod.entity.EntitySoulPearl;
import lu.kremi151.minamod.entity.EntityTurtle;
import lu.kremi151.minamod.entity.EntityWookie;
import lu.kremi151.minamod.packet.message.MessageCoinBag;
import lu.kremi151.minamod.packet.message.MessageJetpack;
import lu.kremi151.minamod.packet.message.MessageOpenGui;
import lu.kremi151.minamod.packet.message.MessageSetScreenLayer;
import lu.kremi151.minamod.packet.message.MessageShowOverlay;
import lu.kremi151.minamod.packet.message.MessageSpawnParticleEffect;
import lu.kremi151.minamod.packet.message.MessageUpdateTileEntity;
import lu.kremi151.minamod.packet.message.MessageUseAmulet;
import lu.kremi151.minamod.packet.message.MessageUseElevator;
import lu.kremi151.minamod.packet.message.handler.MessageCoinBagHandler;
import lu.kremi151.minamod.packet.message.handler.MessageJetpackHandler;
import lu.kremi151.minamod.packet.message.handler.MessageOpenGuiHandler;
import lu.kremi151.minamod.packet.message.handler.MessageSetScreenLayerHandler;
import lu.kremi151.minamod.packet.message.handler.MessageShowOverlayHandler;
import lu.kremi151.minamod.packet.message.handler.MessageSpawnParticleEffectHandler;
import lu.kremi151.minamod.packet.message.handler.MessageUpdateTileEntityHandler;
import lu.kremi151.minamod.packet.message.handler.MessageUseAmuletHandler;
import lu.kremi151.minamod.packet.message.handler.MessageUseElevatorHandler;
import lu.kremi151.minamod.proxy.CommonProxy;
import lu.kremi151.minamod.util.FMLEventListeners;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.MappingsHandler;
import lu.kremi151.minamod.util.MinaGuiHandler;
import lu.kremi151.minamod.util.MinaModConfiguration;
import lu.kremi151.minamod.util.MinaTickEventHandler;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.Once;
import lu.kremi151.minamod.util.OreInjectorManager;
import lu.kremi151.minamod.util.VillagerHelper;
import lu.kremi151.minamod.util.eventlisteners.BlockEvents;
import lu.kremi151.minamod.util.eventlisteners.EntityEvents;
import lu.kremi151.minamod.util.eventlisteners.PlayerSpecificEvents;
import lu.kremi151.minamod.util.eventlisteners.TerrainEventListeners;
import lu.kremi151.minamod.util.eventlisteners.WorldEvents;
import lu.kremi151.minamod.worldgen.WorldGenerators;
import lu.kremi151.minamod.worldprovider.WorldProviderOverworldHook;
import net.minecraft.block.Block;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.permission.PermissionAPI;

@Mod(modid = MinaMod.MODID, name = MinaMod.MODNAME, version = MinaMod.VERSION, guiFactory = "lu.kremi151.minamod.util.MinaGuiFactory")
public class MinaMod {
	
	public static final String MODNAME = "Mina Mod";
	public static final String MODID = "minamod";
	public static final String VERSION = "@VERSION@";
	public static final String MC_VERSION = "@MCV@";
	
	private static final boolean MOD_DEV_ANALYTICS = false;//Only used for mod development

	@SidedProxy(modId = MODID, clientSide = "lu.kremi151.minamod.proxy.ClientProxy", serverSide = "lu.kremi151.minamod.proxy.CommonProxy")
	private static CommonProxy proxy;
	private SimpleNetworkWrapper networkWrapper;
	@Instance(MODID)
	private static MinaMod instance;
	private MinaTickEventHandler tickHandler;
	private Logger logger;

	private static MinaModConfiguration config;
	private MinaGuiHandler guiHandler = new MinaGuiHandler();

	private MinecraftServer theServer;
	
	public static final Once<File> minaConfigPath = Once.ready();

	@EventHandler
	public void init(FMLInitializationEvent event) throws IllegalArgumentException, IllegalAccessException { 
		proxy.registerKeyBindings();

		tickHandler = new MinaTickEventHandler(this);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

		getProxy().registerItemAndBlockColors();
		
		WorldGenerators.registerWorldGenerators();
		OreInjectorManager.init();

		TerrainEventListeners terrainEventListener = new TerrainEventListeners(this);

		MinaBlocks.setFireInfos();

		if(FeatureList.enable_plate){
			GameRegistry.registerTileEntity(TileEntityPlate.class, createDottedIdentifier("plate"));
		}
		GameRegistry.registerTileEntity(TileEntityLetterbox.class, createDottedIdentifier("letterbox"));
		GameRegistry.registerTileEntity(TileEntityAutoFeeder.class, createDottedIdentifier("auto_feeder"));
		GameRegistry.registerTileEntity(TileEntityGiftBox.class, createDottedIdentifier("gift_box"));
		GameRegistry.registerTileEntity(TileEntityLock.class, createDottedIdentifier("keylock"));
		GameRegistry.registerTileEntity(TileEntityCollector.class, createDottedIdentifier("collector"));
		GameRegistry.registerTileEntity(TileEntitySieve.class, createDottedIdentifier("sieve"));
		if(FeatureList.enable_cable){
			GameRegistry.registerTileEntity(TileEntityCable.class, createDottedIdentifier("cable"));
			GameRegistry.registerTileEntity(TileEntityEnergySource.class, createDottedIdentifier("energy_source"));
		}
		GameRegistry.registerTileEntity(TileEntityHerbCrop.class, createDottedIdentifier("herb"));
		GameRegistry.registerTileEntity(TileEntityElevatorControl.class, createDottedIdentifier("elevator_control"));
		GameRegistry.registerTileEntity(TileEntityCampfire.class, createDottedIdentifier("camp_fire"));

		EntityRegistry.registerModEntity(EntityIceGolhem.ID, EntityIceGolhem.class, createDottedIdentifier(EntityIceGolhem.entityId), IDRegistry.entityIceGolhemId,
				this, 64, 1, true, MinaUtils.convertRGBToDecimal(17, 173, 238),
				MinaUtils.convertRGBToDecimal(11, 112, 179));

		if(FeatureList.enable_soul_pearls){
			EntityRegistry.registerModEntity(EntitySoulPearl.ID, EntitySoulPearl.class, createDottedIdentifier(EntitySoulPearl.entityId), IDRegistry.entitySoulPearlId, this, 64,
					1, true);
		}

		if(FeatureList.enable_ice_altar){
			EntityRegistry.registerModEntity(EntityIceSentinel.ID, EntityIceSentinel.class, createDottedIdentifier(EntityIceSentinel.entityId),
					IDRegistry.entityIceSentinelId, this, 64, 1, true);
		}

		EntityRegistry.registerModEntity(EntityFrostBall.ID, EntityFrostBall.class, createDottedIdentifier(EntityFrostBall.entityId), IDRegistry.entityFrostBallId, this, 64,
				1, true);

		EntityRegistry.registerModEntity(EntityTurtle.ID, EntityTurtle.class, createDottedIdentifier(EntityTurtle.entityId), IDRegistry.entityTurtleId, this,
				64, 1, true, MinaUtils.convertRGBToDecimal(217, 191, 0), MinaUtils.convertRGBToDecimal(110, 191, 0));
		EntityRegistry.addSpawn(EntityTurtle.class, 6, 2, 4, EnumCreatureType.CREATURE, Biomes.BEACH,
				Biomes.STONE_BEACH);

		EntityRegistry.registerModEntity(EntityBee.ID, EntityBee.class, createDottedIdentifier(EntityBee.entityId), IDRegistry.entityBeeId, this, 32, 1,
				true);
		//TODO: too much framedropping
		if(FeatureList.enable_bees)EntityRegistry.addSpawn(EntityBee.class, 1, 1, 2, EnumCreatureType.AMBIENT, Biomes.PLAINS);

		EntityRegistry.registerModEntity(EntityPenguin.ID, EntityPenguin.class, createDottedIdentifier(EntityPenguin.entityId), IDRegistry.entityPenguinId, this,
				64, 1, true, MinaUtils.convertRGBToDecimal(42, 42, 42), MinaUtils.convertRGBToDecimal(229, 229, 229));
		EntityRegistry.addSpawn(EntityPenguin.class, 6, 2, 4, EnumCreatureType.CREATURE, Biomes.ICE_PLAINS);

		EntityRegistry.registerModEntity(EntityWookie.ID, EntityWookie.class, createDottedIdentifier(EntityWookie.entityId), IDRegistry.entityWookieId, this,
				64, 1, true, MinaUtils.convertRGBToDecimal(106, 42, 29), MinaUtils.convertRGBToDecimal(0, 0, 0));
		EntityRegistry.addSpawn(EntityWookie.class, 5, 3, 6, EnumCreatureType.CREATURE, Biomes.FOREST);
		
		EntityRegistry.registerModEntity(EntityFish.ID, EntityFish.class, EntityFish.entityId, IDRegistry.entityFishId, this,
				64, 1, true, MinaUtils.convertRGBToDecimal(255, 0, 102), MinaUtils.convertRGBToDecimal(51, 204, 255));

		println("Registering recipes...");
		MinaRecipes.initCraftingRecipes(this);
		MinaRecipes.initFurnaceRecipes(this);
		MinaRecipes.initBrewingRecipes(this);

		MinecraftForge.EVENT_BUS.register(new BlockEvents(this));
		MinecraftForge.EVENT_BUS.register(new EntityEvents(this));
		MinecraftForge.EVENT_BUS.register(new PlayerSpecificEvents(this));
		MinecraftForge.EVENT_BUS.register(new WorldEvents(this));
		MinecraftForge.TERRAIN_GEN_BUS.register(terrainEventListener);
		MinecraftForge.EVENT_BUS.register(terrainEventListener);
		MinecraftForge.EVENT_BUS.register(tickHandler);

		proxy.registerBuildInBlocks();
		proxy.registerOverlays();
		
		Field fields[] = MinaPermissions.class.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			MinaPermission node = f.getAnnotation(MinaPermission.class);
			if(node != null && f.getType() == String.class){
				try {
					String pnode = (String) f.get(null);
					if(getMinaConfig().isDebugging()){
						println("Registering permission node \"%s\"...", pnode);
					}
					PermissionAPI.registerNode(pnode, node.lvl(), node.desc());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { // NO_UCD (unused
																// code)
		// BiomeDictionary.registerAllBiomes();
		
		logger.info("MinaMod inside! #MakeMinecraftGreatAgain");
		
		if(MOD_DEV_ANALYTICS){
			for(Field field : MinaBlocks.class.getDeclaredFields()){
				if(net.minecraft.block.Block.class.isAssignableFrom(field.getType())){
					try {
						net.minecraft.block.Block theBlock = (Block) field.get(null);
						Field blockHardness = Block.class.getDeclaredField("blockHardness");
						Field blockResistance = Block.class.getDeclaredField("blockResistance");
						blockHardness.setAccessible(true);
						blockResistance.setAccessible(true);
						if(((Float)blockHardness.get(theBlock)) == 0.0f){
							System.out.println("### WARNING: No blockHardness parameter set for block " + theBlock.getRegistryName() + " (" + field.getName() + ")");
						}
						if(((Float)blockResistance.get(theBlock)) == 0.0f){
							System.out.println("### WARNING: No blockResistance parameter set for block " + theBlock.getRegistryName() + " (" + field.getName() + ")");
						}
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws NoSuchMethodException, SecurityException { // NO_UCD
																											// code)
		this.logger = event.getModLog();

		minaConfigPath.set(new File(event.getModConfigurationDirectory(), "minamod"));
		if(!minaConfigPath.get().exists()){
			minaConfigPath.get().mkdirs();
		}
		
		Configuration config = new Configuration(new File(minaConfigPath.get(), "minamod.cfg"));
		config.load();
		this.config = new MinaModConfiguration(config);
		config.save();

		if (this.config.isDebugging()) {
			println("Debugging enabled");
		}

		debugPrintln("preinit called");

		MinecraftForge.EVENT_BUS.register(new FMLEventListeners());
		proxy.initClientEvents();

		setupNetworkWrapper(event.getSide());

		MinaFluids.registerFluids();
		MinaBlocks.registerOreEntries();
		MinaPotions.register();
		MinaEnchantments.registerEnchantments();
		VillagerHelper.instance().registerVillagers();
		
		MinaCapabilities.init();

		MinaBiomes.init();

		MinaSounds.init();

		proxy.registerRenderers();
		WorldProviderOverworldHook.hookIn();
		
		MinaDataSerializers.register();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) { // NO_UCD (unused
																// code)
		this.theServer = event.getServer();
		ServerCommandManager cm = (ServerCommandManager) theServer.getCommandManager();
		cm.registerCommand(new CommandMinaBase());
	}

	public MinecraftServer getMinecraftServer() {
		return this.theServer;
	}

	public int saveAll(Side side) {
		// TODO
		return 0;
	}

	public static MinaMod getMinaMod() {
		return instance;
	}

	public static MinaModAPI getAPI() { // NO_UCD (unused code)
		return proxy;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static MinaModConfiguration getMinaConfig() {
		return config;
	}

	public SimpleNetworkWrapper getPacketDispatcher() {
		return networkWrapper;
	}

	public MinaTickEventHandler getTickHandler() {
		return tickHandler;
	}

	public static void debugPrintln(String msg) {
		if (config.isDebugging()) {
			getMinaMod().logger.info(msg);
		}
	}
	
	public static void println(String msg, Object... params){
		getMinaMod().logger.info(String.format(msg, params));
	}
	
	public static void errorln(String msg, Object... params){
		getMinaMod().logger.error(String.format(msg, params));
	}
	
	public static Logger getLogger(){
		return getMinaMod().logger;
	}

	private void setupNetworkWrapper(Side side) {
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		registerPackets(side);
	}

	private void registerPackets(Side side) {
		networkWrapper.registerMessage(MessageOpenGuiHandler.class, MessageOpenGui.class, 2, Side.SERVER);
		networkWrapper.registerMessage(MessageOpenGuiHandler.class, MessageOpenGui.class, 2, Side.CLIENT);
		networkWrapper.registerMessage(MessageShowOverlayHandler.class, MessageShowOverlay.class, 3, Side.CLIENT);
		networkWrapper.registerMessage(MessageUseElevatorHandler.class, MessageUseElevator.class, 4, Side.SERVER);
		networkWrapper.registerMessage(MessageSetScreenLayerHandler.class, MessageSetScreenLayer.class, 5, Side.CLIENT);
		networkWrapper.registerMessage(MessageJetpackHandler.class, MessageJetpack.class, 6, Side.SERVER);
		networkWrapper.registerMessage(MessageCoinBagHandler.class, MessageCoinBag.class, 7, Side.SERVER);
		networkWrapper.registerMessage(MessageCoinBagHandler.class, MessageCoinBag.class, 7, Side.CLIENT);
		networkWrapper.registerMessage(MessageUpdateTileEntityHandler.class, MessageUpdateTileEntity.class, 8, Side.CLIENT);
		networkWrapper.registerMessage(MessageUseAmuletHandler.class, MessageUseAmulet.class, 9, Side.SERVER);
		networkWrapper.registerMessage(MessageSpawnParticleEffectHandler.class, MessageSpawnParticleEffect.class, 10, Side.CLIENT);
	}
	
	public static String createDottedIdentifier(String name){
		return MODID + "." + name;
	}
	
	public static ResourceLocation createResourceLocation(String name){
		return new ResourceLocation(MODID, name);
	}
	
	@EventHandler
	public void onMissingMappings(FMLMissingMappingsEvent event){
		MappingsHandler.handleMappings(event);
	}

}
