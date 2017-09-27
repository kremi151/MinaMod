package lu.kremi151.minamod;

import lu.kremi151.minamod.block.BlockAutoCrafter;
import lu.kremi151.minamod.block.BlockAutoFeeder;
import lu.kremi151.minamod.block.BlockBambusCrop;
import lu.kremi151.minamod.block.BlockBerryCrop;
import lu.kremi151.minamod.block.BlockCable;
import lu.kremi151.minamod.block.BlockCampbench;
import lu.kremi151.minamod.block.BlockCampfire;
import lu.kremi151.minamod.block.BlockChair;
import lu.kremi151.minamod.block.BlockChiliCrop;
import lu.kremi151.minamod.block.BlockCoconut;
import lu.kremi151.minamod.block.BlockCollector;
import lu.kremi151.minamod.block.BlockCombined;
import lu.kremi151.minamod.block.BlockCustom;
import lu.kremi151.minamod.block.BlockCustomAxis;
import lu.kremi151.minamod.block.BlockCustomBush;
import lu.kremi151.minamod.block.BlockCustomCake;
import lu.kremi151.minamod.block.BlockCustomOre;
import lu.kremi151.minamod.block.BlockCustomRotatedPillar;
import lu.kremi151.minamod.block.BlockCustomStainedGlass;
import lu.kremi151.minamod.block.BlockCustomStairs;
import lu.kremi151.minamod.block.BlockDimmableLight;
import lu.kremi151.minamod.block.BlockEffectBush;
import lu.kremi151.minamod.block.BlockElevatorControl;
import lu.kremi151.minamod.block.BlockElevatorFloor;
import lu.kremi151.minamod.block.BlockEnergySource;
import lu.kremi151.minamod.block.BlockFilter;
import lu.kremi151.minamod.block.BlockGiftBox;
import lu.kremi151.minamod.block.BlockHerb;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockLetterbox;
import lu.kremi151.minamod.block.BlockLock;
import lu.kremi151.minamod.block.BlockMinaLeaf;
import lu.kremi151.minamod.block.BlockMinaLeafBase;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.block.BlockMinaWoodSlab;
import lu.kremi151.minamod.block.BlockMinaWoodStairs;
import lu.kremi151.minamod.block.BlockNamieFlower;
import lu.kremi151.minamod.block.BlockPalmLeaves;
import lu.kremi151.minamod.block.BlockPalmLog;
import lu.kremi151.minamod.block.BlockPlate;
import lu.kremi151.minamod.block.BlockPumpkinLantern;
import lu.kremi151.minamod.block.BlockQuicksand;
import lu.kremi151.minamod.block.BlockRedstoneCrossroad;
import lu.kremi151.minamod.block.BlockRhubarb;
import lu.kremi151.minamod.block.BlockSieve;
import lu.kremi151.minamod.block.BlockSlotMachine;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import lu.kremi151.minamod.block.BlockStool;
import lu.kremi151.minamod.block.BlockStrawberryCrop;
import lu.kremi151.minamod.block.BlockTable;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


public class MinaBlocks { // NO_UCD (unused code)

	public static final BlockTable OAK_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("oak_table");
	public static final BlockTable SPRUCE_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("spruce_table");
	public static final BlockTable BIRCH_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("birch_table");
	public static final BlockTable JUNGLE_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("jungle_table");
	public static final BlockTable ACACIA_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("acacia_table");
	public static final BlockTable DARK_OAK_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("dark_oak_table");
	public static final BlockTable PEPPEL_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("peppel_table");
	public static final BlockTable CHESTNUT_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("chestnut_table");
	public static final BlockTable CHERRY_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("cherry_table");
	public static final BlockTable COTTON_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Wood)
			.setUnlocalizedName("cotton_table");
	public static final BlockTable STONE_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Stone)
			.setUnlocalizedName("stone_table");
	public static final BlockTable IRON_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("iron_table");
	public static final BlockTable GOLD_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("gold_table");
	public static final BlockTable LAPIS_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("lapis_table");
	public static final BlockTable OBSIDIAN_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("obsidian_table");
	public static final BlockTable DIAMOND_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("diamond_table");
	public static final BlockTable ICE_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("ice_table");
	public static final BlockTable CITRIN_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("citrin_table");
	public static final BlockTable SPONGE_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("sponge_table");
	public static final BlockTable QUARTZ_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("quartz_table");
	public static final BlockTable EMERALD_TABLE = (BlockTable) new BlockTable(BlockTable.TableType.Rare)
			.setUnlocalizedName("emerald_table");
	public static final BlockStool OAK_STOOL = (BlockStool) new BlockStool().setUnlocalizedName("stool");
	public static final BlockStool DARK_OAK_STOOL = (BlockStool) new BlockStool().setUnlocalizedName("stool");
	public static final BlockStool ACACIA_STOOL = (BlockStool) new BlockStool().setUnlocalizedName("stool");
	public static final BlockNamieFlower NAMIE_FLOWER = new BlockNamieFlower();
	public static final BlockCustomOre CITRIN_ORE = (BlockCustomOre) new BlockCustomOre().setHardness(4.0F)
			.setResistance(5.0F).setUnlocalizedName("citrin_ore");
	public static final Block CITRIN_BLOCK = new BlockCustom(Material.IRON).setUnlocalizedName("citrin_block")
			.setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final BlockHoneycomb HONEYCOMB = (BlockHoneycomb) new BlockHoneycomb()
			.setUnlocalizedName("honeycomb");
	public static final Block HONEY_CAKE = new BlockCustomCake().setUnlocalizedName("honeycake");
	public static final BlockStrawberryCrop STRAWBERRY_CROP = new BlockStrawberryCrop();
	public static final Block CHOCOLATE_CAKE = new BlockCustomCake().setUnlocalizedName("chocolatecake");
	public static final Block STRAWBERRY_CAKE = new BlockCustomCake().setUnlocalizedName("strawberrycake");
	public static final Block RHUBARB_PLANT = new BlockRhubarb().setUnlocalizedName("rhubarb_plant");
	public static final Block CREEPER_CAKE = new BlockCustomCake().setFoodAmountPerSlice(4)
			.setUnlocalizedName("creepercake");
	public static final Block FROZEN_STONE = new BlockCustom(Material.ROCK).setUnlocalizedName("frozen_stone")
			.setHardness(1.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block FROZEN_BRICK = new BlockCustom(Material.ROCK).setUnlocalizedName("frozen_brick")
			.setHardness(3f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block FROZEN_GLOWSTONE = new BlockCustom(Material.GLASS).setSoundType(SoundType.GLASS)
			.setUnlocalizedName("frozen_glowstone").setHardness(0.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
			.setLightLevel(1f);
	public static final Block BAMBUS_CROP = new BlockBambusCrop().setUnlocalizedName("bambus_crop");
	public static final Block EVER_ICE_BLOCK = new BlockCustom(Material.ROCK).setSoundType(SoundType.GLASS).setUnlocalizedName("ever_ice")
			.setHardness(8f).setResistance(11f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block RHUBARB_PIE = new BlockCustomCake().setBlockHeight(4f / 16f)
			.setUnlocalizedName("rhubarbpie");
	public static final Block LITTLE_BUSH = new BlockCustomBush().setCreativeTab(CreativeTabs.DECORATIONS).setUnlocalizedName("little_bush");
	public static final Block OPAQUE_BUSH = new BlockCustomBush().setCreativeTab(CreativeTabs.DECORATIONS).setUnlocalizedName("opaque_bush");
	public static final Block BAMBUS_BLOCK = new BlockCustomAxis(Material.CLOTH, MapColor.GRASS).setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
			.setUnlocalizedName("bambus_block");
	public static final BlockEffectBush EFFECT_BUSH = (BlockEffectBush) new BlockEffectBush()
			.setUnlocalizedName("effect_bush");
	public static final Block KEY_LOCK = new BlockLock().setUnlocalizedName("keylock");
	public static final Block ICE_ALTAR = new BlockIceAltar().setUnlocalizedName("ice_altar")
			.setCreativeTab(CreativeTabs.DECORATIONS);
	public static final Block PLATINUM_ORE = new BlockCustomOre().setHardness(4.5F).setResistance(6.0F)
			.setUnlocalizedName("platinum_ore");
	public static final Block PLATINUM_BLOCK = new BlockCustom(Material.IRON).setUnlocalizedName("platinum_block")
			.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(3.5f).setResistance(6f);
	public static final BlockMinaPlanks PLANKS = (BlockMinaPlanks) new BlockMinaPlanks().setSoundType(SoundType.WOOD).setHardness(2.0f).setResistance(5.0f).setUnlocalizedName("mina_planks");
	public static final BlockStandaloneLog LOG_PEPPEL = (BlockStandaloneLog) new BlockStandaloneLog(BlockMinaPlanks.EnumType.PEPPEL).setUnlocalizedName("log_peppel");
	public static final BlockStandaloneLog LOG_COTTON = (BlockStandaloneLog) new BlockStandaloneLog(BlockMinaPlanks.EnumType.COTTON).setUnlocalizedName("log_cotton");
	public static final BlockStandaloneLog LOG_CHESTNUT = (BlockStandaloneLog) new BlockStandaloneLog(BlockMinaPlanks.EnumType.CHESTNUT).setUnlocalizedName("log_chestnut");
	public static final BlockStandaloneLog LOG_CHERRY = (BlockStandaloneLog) new BlockStandaloneLog(BlockMinaPlanks.EnumType.CHERRY).setUnlocalizedName("log_cherry");
	public static final BlockPalmLog LOG_PALM = (BlockPalmLog) new BlockPalmLog(BlockMinaPlanks.EnumType.PALM).setUnlocalizedName("log_palm");
	public static final BlockMinaLeaf MINA_LEAVES_A = (BlockMinaLeaf) new BlockMinaLeaf.A().setUnlocalizedName("mina_leaves_a");
	//public static final BlockMinaLeaf MINA_LEAVES_B = (BlockMinaLeaf) new BlockMinaLeaf.B().setUnlocalizedName("mina_leaves_b");
	public static final BlockMinaLeafBase PALM_LEAVES = (BlockMinaLeafBase) new BlockPalmLeaves().setUnlocalizedName("palm_leaves");
	public static final BlockMinaSapling SAPLING = (BlockMinaSapling) new BlockMinaSapling().setUnlocalizedName("mina_sapling");
	public static final BlockMinaWoodSlab WOODEN_SLAB = (BlockMinaWoodSlab) new BlockMinaWoodSlab.Half().setUnlocalizedName("woodSlab");
	public static final BlockMinaWoodSlab DOUBLE_WOODEN_SLAB = (BlockMinaWoodSlab) new BlockMinaWoodSlab.Double().setUnlocalizedName("woodSlab");
	public static final BlockMinaWoodStairs PEPPEL_STAIRS = (BlockMinaWoodStairs) new BlockMinaWoodStairs(BlockMinaPlanks.EnumType.PEPPEL).setUnlocalizedName("peppel_stairs");
	public static final BlockMinaWoodStairs COTTON_STAIRS = (BlockMinaWoodStairs) new BlockMinaWoodStairs(BlockMinaPlanks.EnumType.COTTON).setUnlocalizedName("cotton_stairs");
	public static final BlockMinaWoodStairs CHESTNUT_STAIRS = (BlockMinaWoodStairs) new BlockMinaWoodStairs(BlockMinaPlanks.EnumType.CHESTNUT).setUnlocalizedName("chestnut_stairs");
	public static final BlockMinaWoodStairs CHERRY_STAIRS = (BlockMinaWoodStairs) new BlockMinaWoodStairs(BlockMinaPlanks.EnumType.CHERRY).setUnlocalizedName("cherry_stairs");
	public static final BlockMinaWoodStairs PALM_STAIRS = (BlockMinaWoodStairs) new BlockMinaWoodStairs(BlockMinaPlanks.EnumType.PALM).setUnlocalizedName("palm_stairs");
	
	public static final BlockPlate PLATE = (BlockPlate) new BlockPlate().setUnlocalizedName("plate");
	public static final Block OAK_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_oak");
	public static final Block SPRUCE_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_spruce");
	public static final Block BIRCH_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_birch");
	public static final Block JUNGLE_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_jungle");
	public static final Block ACACIA_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_acacia");
	public static final Block BIG_OAK_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_big_oak");
	public static final Block CHESTNUT_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_chestnut");
	public static final Block CHERRY_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_cherry");
	public static final Block PEPPEL_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_peppel");
	public static final Block COTTON_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_cotton");
	public static final Block PALM_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_palm");
	public static final Block BAMBUS_LETTERBOX = new BlockLetterbox().setUnlocalizedName("letterbox_bambus");
	
	public static final BlockCustomOre SAPPHIRE_ORE = (BlockCustomOre) new BlockCustomOre().setHardness(4.0F)
			.setResistance(5.0F).setUnlocalizedName("sapphire_ore");
	public static final BlockCustomOre RUBY_ORE = (BlockCustomOre) new BlockCustomOre().setHardness(4.0F)
			.setResistance(5.0F).setUnlocalizedName("ruby_ore");
	public static final Block SAPPHIRE_BLOCK = new BlockCustom(Material.IRON).setUnlocalizedName("sapphire_block")
			.setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final Block RUBY_BLOCK = new BlockCustom(Material.IRON).setUnlocalizedName("ruby_block")
			.setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	public static final BlockElevatorFloor ELEVATOR_FLOOR = (BlockElevatorFloor) new BlockElevatorFloor()
			.setUnlocalizedName("elevator_floor");
	public static final Block ELEVATOR_CONTROL = new BlockElevatorControl()
			.setCreativeTab(MinaCreativeTabs.TECHNOLOGY).setUnlocalizedName("elevator_control");
	public static final BlockStairs FROZEN_BRICK_STAIRS = (BlockStairs) new BlockCustomStairs(MinaBlocks.FROZEN_BRICK.getDefaultState()).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("frozen_brick_stairs");
	public static final BlockAutoFeeder AUTO_FEEDER = (BlockAutoFeeder) new BlockAutoFeeder().setUnlocalizedName("autofeeder");
	public static final BlockRedstoneCrossroad REDSTONE_CROSSROAD = (BlockRedstoneCrossroad) new BlockRedstoneCrossroad().setUnlocalizedName("redstone_crossroad");
	public static final BlockCable CABLE = (BlockCable) new BlockCable().setUnlocalizedName("cable");
	public static final BlockCustomStainedGlass MILKY_GLASS = (BlockCustomStainedGlass) new BlockCustomStainedGlass().setHardness(0.3F).setUnlocalizedName("milky_glass");
	public static final BlockCustomStainedGlass LIT_MILKY_GLASS = (BlockCustomStainedGlass) new BlockCustomStainedGlass().setHardness(0.5f).setUnlocalizedName("lit_milky_glass").setLightLevel(1f);
	public static final BlockGiftBox GIFT_BOX = (BlockGiftBox) new BlockGiftBox().setHardness(0.5f).setUnlocalizedName("gift_box");
	public static final BlockBerryCrop DOGE_CROP = (BlockBerryCrop) new BlockBerryCrop(BlockBerryCrop.BerryType.DOGE);
	public static final BlockBerryCrop KEVIKUS_CROP = (BlockBerryCrop) new BlockBerryCrop(BlockBerryCrop.BerryType.KEVIKUS);
	public static final BlockBerryCrop TRACIUS_CROP = (BlockBerryCrop) new BlockBerryCrop(BlockBerryCrop.BerryType.TRACIUS);
	public static final BlockChiliCrop CHILI_CROP = (BlockChiliCrop) new BlockChiliCrop();
	public static final BlockCombined COBBLEBRICKS = (BlockCombined) new BlockCombined(Material.ROCK).setSoundType(SoundType.STONE).setUnlocalizedName("cobblebrick_stone").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(4.0F).setResistance(6.0F);
	public static final BlockSlotMachine SLOT_MACHINE = (BlockSlotMachine) new BlockSlotMachine().setHardness(4.5F).setResistance(6.0F).setCreativeTab(MinaCreativeTabs.TECHNOLOGY).setUnlocalizedName("slot_machine");
	public static final Block COLLECTOR = new BlockCollector().setUnlocalizedName("collector");
	public static final BlockCampfire CAMPFIRE = (BlockCampfire) new BlockCampfire().setHardness(2.0f).setResistance(5.0f).setUnlocalizedName("campfire");
	public static final BlockChair OAK_CHAIR = (BlockChair) new BlockChair().setUnlocalizedName("oak_chair");
	public static final BlockSieve SIEVE = (BlockSieve) new BlockSieve().setUnlocalizedName("sieve");
	public static final BlockHerb HERB_CROP = new BlockHerb();
	public static final BlockCampbench CAMPBENCH = (BlockCampbench) new BlockCampbench().setUnlocalizedName("campbench");
	public static final BlockQuicksand QUICKSAND = (BlockQuicksand) new BlockQuicksand().setUnlocalizedName("quicksand");
	public static final BlockCoconut COCONUT = (BlockCoconut) new BlockCoconut().setUnlocalizedName("coconut");
	public static final BlockDimmableLight DIMMABLE_LIGHT = (BlockDimmableLight) new BlockDimmableLight().setUnlocalizedName("dimmable_lamp");
	public static final BlockRotatedPillar WABE_BLOCK = (BlockRotatedPillar) new BlockCustomRotatedPillar(Material.CLOTH, MapColor.BROWN).setUnlocalizedName("wabe_block").setCreativeTab(CreativeTabs.DECORATIONS);
	public static final BlockFilter FILTER = (BlockFilter) new BlockFilter().setHardness(3.0F).setResistance(8.0F).setUnlocalizedName("filter");
	public static final BlockAutoCrafter AUTO_CRAFTER = (BlockAutoCrafter) new BlockAutoCrafter().setHardness(3.0F).setResistance(8.0F).setUnlocalizedName("autocrafter");
	public static final BlockCustomOre RARE_EARTH_ORE = (BlockCustomOre) new BlockCustomOre().setHardness(4.0F)
			.setResistance(5.0F);
	public static final BlockPumpkinLantern PUMPKIN_LANTERN = (BlockPumpkinLantern) new BlockPumpkinLantern().setUnlocalizedName("pumpkin_lantern");
	
	static void registerBlocks(IRegistrationInterface<Block, BlockRegistrationHandler> registry) {
		BlockTable.registerTableBlocks(registry);
		BlockStool.registerStoolBlocks(registry);

		registry.register(NAMIE_FLOWER, "namie_flower_crop").blockOnly().submit();
		registry.register(CITRIN_ORE, "citrin_ore").ore("oreCitrin").submit();
		registry.register(CITRIN_BLOCK, "citrin_block").ore("blockCitrin").submit();
		registry.register(HONEYCOMB, "honeycomb").ore("honeycomb").submit();
		registry.register(HONEY_CAKE, "honeycake").submit();
		registry.register(STRAWBERRY_CROP, "strawberry_crop").submit();
		registry.register(STRAWBERRY_CAKE, "strawberrycake").submit();
		registry.register(CHOCOLATE_CAKE, "chocolatecake").submit();
		registry.register(RHUBARB_PLANT, "rhubarb").blockOnly().submit();
		registry.register(CREEPER_CAKE, "creepercake").submit();
		registry.register(FROZEN_STONE, "frozen_stone").submit();
		registry.register(FROZEN_BRICK, "frozen_brick").submit();
		registry.register(FROZEN_GLOWSTONE, "frozen_glowstone").submit();
		BlockCustomStairs.registerStairBlocks(registry);
		registry.register(BAMBUS_CROP, "bambus_crop").blockOnly().submit();
		registry.register(EVER_ICE_BLOCK, "ever_ice").submit();
		registry.register(RHUBARB_PIE, "rhubarbpie").submit();
		registry.register(LITTLE_BUSH, "little_bush").submit();
		registry.register(OPAQUE_BUSH, "opaque_bush").submit();
		registry.register(BAMBUS_BLOCK, "bambus_block").submit();
		registry.register(EFFECT_BUSH, "effect_bush").blockOnly().submit();
		registry.register(KEY_LOCK, "keylock").submit();
		if(FeatureList.enable_ice_altar){
			registry.register(ICE_ALTAR, "ice_altar").submit();
		}
		registry.register(PLATINUM_ORE, "platinum_ore").ore("orePlatinum").submit();
		registry.register(PLATINUM_BLOCK, "platinum_block").ore("blockPlatinum").submit();
		registry.register(PLANKS, "mina_planks").blockOnly().submit();
		registry.register(LOG_PEPPEL, "log_peppel").submit();
		registry.register(LOG_COTTON, "log_cotton").submit();
		registry.register(LOG_CHESTNUT, "log_chestnut").submit();
		registry.register(LOG_CHERRY, "log_cherry").submit();
		registry.register(MINA_LEAVES_A, "mina_leaves_a").blockOnly().submit();
		//proxy.registerBlockOnly(MINA_LEAVES_B, "mina_leaves_b");
		registry.register(PALM_LEAVES, "palm_leaves").blockOnly().submit();
		registry.register(SAPLING, "mina_sapling").blockOnly().submit();
		registry.register(HERB_CROP, "herb").blockOnly().submit();
		
		if(FeatureList.enable_plate){
			registry.register(PLATE, "plate").submit();
		}
		registry.register(OAK_LETTERBOX, "letterbox_oak").submit();
		registry.register(SPRUCE_LETTERBOX, "letterbox_spruce").submit();
		registry.register(BIRCH_LETTERBOX, "letterbox_birch").submit();
		registry.register(JUNGLE_LETTERBOX, "letterbox_jungle").submit();
		registry.register(ACACIA_LETTERBOX, "letterbox_acacia").submit();
		registry.register(BIG_OAK_LETTERBOX, "letterbox_big_oak").submit();
		registry.register(CHESTNUT_LETTERBOX, "letterbox_chestnut").submit();
		registry.register(CHERRY_LETTERBOX, "letterbox_cherry").submit();
		registry.register(PEPPEL_LETTERBOX, "letterbox_peppel").submit();
		registry.register(COTTON_LETTERBOX, "letterbox_cotton").submit();
		registry.register(PALM_LETTERBOX, "letterbox_palm").submit();
		registry.register(BAMBUS_LETTERBOX, "letterbox_bambus").submit();
		registry.register(RUBY_ORE, "ruby_ore").ore("oreRuby").submit();
		registry.register(SAPPHIRE_ORE, "sapphire_ore").ore("oreSapphire").submit();
		registry.register(RUBY_BLOCK, "ruby_block").ore("blockRuby").submit();
		registry.register(SAPPHIRE_BLOCK, "sapphire_block").ore("blockSapphire").submit();
		registry.register(ELEVATOR_FLOOR, "elevator_floor").blockOnly().submit();
		registry.register(ELEVATOR_CONTROL, "elevator_control").submit();
		registry.register(AUTO_FEEDER, "autofeeder").submit();
		if(FeatureList.enable_redstone_crossroad){
			registry.register(REDSTONE_CROSSROAD, "redstone_crossroad").submit();
		}
		if(FeatureList.enable_cable){
			registry.register(CABLE, "cable").submit();
			registry.register(new BlockEnergySource().setUnlocalizedName("energy_source"), "energy_source").submit();
		}
		registry.register(MILKY_GLASS, "milky_glass").blockOnly().submit();
		registry.register(LIT_MILKY_GLASS, "lit_milky_glass").blockOnly().submit();
		registry.register(GIFT_BOX, "gift_box").blockOnly().submit();
		registry.register(DOGE_CROP, "doge_crop").blockOnly().submit();
		registry.register(KEVIKUS_CROP, "kevikus_crop").blockOnly().submit();
		registry.register(TRACIUS_CROP, "tracius_crop").blockOnly().submit();
		registry.register(CHILI_CROP, "chili_crop").blockOnly().submit();
		registry.register(COBBLEBRICKS, "cobblebrick_stone").blockOnly().submit();
		registry.register(COLLECTOR, "collector").submit();
		registry.register(CAMPFIRE, "campfire").submit();
		registry.register(SLOT_MACHINE, "slot_machine").submit();
		if(FeatureList.enable_chairs)registry.register(OAK_CHAIR, "oak_chair").submit();
		registry.register(SIEVE, "sieve").submit();
		registry.register(CAMPBENCH, "campbench").blockOnly().submit();
		registry.register(QUICKSAND, "quicksand").submit();
		registry.register(LOG_PALM, "log_palm").submit();
		registry.register(WOODEN_SLAB, "mina_wooden_slab").blockOnly().submit();
		registry.register(DOUBLE_WOODEN_SLAB, "mina_double_wooden_slab").blockOnly().submit();
		registry.register(PEPPEL_STAIRS, "peppel_stairs").submit();
		registry.register(COTTON_STAIRS, "cotton_stairs").submit();
		registry.register(CHESTNUT_STAIRS, "chestnut_stairs").submit();
		registry.register(CHERRY_STAIRS, "cherry_stairs").submit();
		registry.register(PALM_STAIRS, "palm_stairs").submit();
		registry.register(COCONUT, "coconut").ore("coconut").submit();
		registry.register(DIMMABLE_LIGHT, "dimmable_lamp").submit();
		registry.register(WABE_BLOCK, "wabe_block").submit();
		registry.register(FILTER, "filter").submit();
		registry.register(AUTO_CRAFTER, "autocrafter").submit();
		registry.register(RARE_EARTH_ORE, "rare_earth_ore").blockOnly().submit();
		registry.register(PUMPKIN_LANTERN, "pumpkin_lantern").submit();
	}
	
	static void registerOreEntries(){
		OreDictionary.registerOre("plankWood", new ItemStack(PLANKS, 1, OreDictionary.WILDCARD_VALUE));
	}

	static void setFireInfos() {
		Blocks.FIRE.setFireInfo(EFFECT_BUSH, 200, 30);
		BlockCustomStairs.registerFireInfos();
		BlockTable.registerFireInfos();
		Blocks.FIRE.setFireInfo(LOG_PEPPEL, 5, 5);
		Blocks.FIRE.setFireInfo(LOG_COTTON, 5, 5);
		Blocks.FIRE.setFireInfo(LOG_CHESTNUT, 5, 5);
		Blocks.FIRE.setFireInfo(LOG_CHERRY, 5, 5);
		Blocks.FIRE.setFireInfo(LOG_PALM, 5, 5);
		Blocks.FIRE.setFireInfo(PLANKS, 5, 20);
		Blocks.FIRE.setFireInfo(MINA_LEAVES_A, 30, 60);
		Blocks.FIRE.setFireInfo(PALM_LEAVES, 30, 60);
		Blocks.FIRE.setFireInfo(BAMBUS_BLOCK, 6, 50);
		Blocks.FIRE.setFireInfo(WOODEN_SLAB, 5, 20);
		Blocks.FIRE.setFireInfo(DOUBLE_WOODEN_SLAB, 5, 20);
	}
}
