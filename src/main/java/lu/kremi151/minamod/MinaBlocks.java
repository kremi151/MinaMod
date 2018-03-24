package lu.kremi151.minamod;

import java.lang.reflect.Field;

import lu.kremi151.minamod.block.BlockAccumulator;
import lu.kremi151.minamod.block.BlockAutoCrafter;
import lu.kremi151.minamod.block.BlockAutoFeeder;
import lu.kremi151.minamod.block.BlockBambusCrop;
import lu.kremi151.minamod.block.BlockBerryCrop;
import lu.kremi151.minamod.block.BlockBook;
import lu.kremi151.minamod.block.BlockCable;
import lu.kremi151.minamod.block.BlockCampbench;
import lu.kremi151.minamod.block.BlockCampfire;
import lu.kremi151.minamod.block.BlockChair;
import lu.kremi151.minamod.block.BlockChiliCrop;
import lu.kremi151.minamod.block.BlockCoalCompressor;
import lu.kremi151.minamod.block.BlockCoconut;
import lu.kremi151.minamod.block.BlockCollector;
import lu.kremi151.minamod.block.BlockCombined;
import lu.kremi151.minamod.block.BlockCopperBlock;
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
import lu.kremi151.minamod.block.BlockEnergyToRedstone;
import lu.kremi151.minamod.block.BlockFilter;
import lu.kremi151.minamod.block.BlockGenerator;
import lu.kremi151.minamod.block.BlockGiftBox;
import lu.kremi151.minamod.block.BlockGravestone;
import lu.kremi151.minamod.block.BlockHerb;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockLetterbox;
import lu.kremi151.minamod.block.BlockLock;
import lu.kremi151.minamod.block.BlockMinaLeaf;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.block.BlockMinaWoodSlab;
import lu.kremi151.minamod.block.BlockNamieFlower;
import lu.kremi151.minamod.block.BlockOven;
import lu.kremi151.minamod.block.BlockPalmLeaves;
import lu.kremi151.minamod.block.BlockPalmLog;
import lu.kremi151.minamod.block.BlockPumpkinLantern;
import lu.kremi151.minamod.block.BlockQuicksand;
import lu.kremi151.minamod.block.BlockRedstoneCrossroad;
import lu.kremi151.minamod.block.BlockRhubarb;
import lu.kremi151.minamod.block.BlockRubberTree;
import lu.kremi151.minamod.block.BlockSieve;
import lu.kremi151.minamod.block.BlockSlotMachine;
import lu.kremi151.minamod.block.BlockSolarPanel;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import lu.kremi151.minamod.block.BlockStool;
import lu.kremi151.minamod.block.BlockStrawberryCrop;
import lu.kremi151.minamod.block.BlockTable;
import lu.kremi151.minamod.block.BlockWallCable;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(MinaMod.MODID)
public class MinaBlocks {

	public static final Block OAK_TABLE = null;
	public static final Block SPRUCE_TABLE = null;
	public static final Block BIRCH_TABLE = null;
	public static final Block JUNGLE_TABLE = null;
	public static final Block ACACIA_TABLE = null;
	public static final Block DARK_OAK_TABLE = null;
	public static final Block PEPPEL_TABLE = null;
	public static final Block CHESTNUT_TABLE = null;
	public static final Block CHERRY_TABLE = null;
	public static final Block COTTON_TABLE = null;
	public static final Block STONE_TABLE = null;
	public static final Block IRON_TABLE = null;
	public static final Block GOLD_TABLE = null;
	public static final Block LAPIS_TABLE = null;
	public static final Block OBSIDIAN_TABLE = null;
	public static final Block DIAMOND_TABLE = null;
	public static final Block ICE_TABLE = null;
	public static final Block CITRIN_TABLE = null;
	public static final Block SPONGE_TABLE = null;
	public static final Block QUARTZ_TABLE = null;
	public static final Block EMERALD_TABLE = null;
	@ObjectHolder("stool_oak") public static final Block OAK_STOOL = null;
	@ObjectHolder("stool_dark_oak") public static final Block DARK_OAK_STOOL = null;
	@ObjectHolder("stool_acacia") public static final Block ACACIA_STOOL = null;
	@ObjectHolder("namie_flower_crop") public static final Block NAMIE_FLOWER = null;
	public static final Block CITRIN_ORE = null;
	public static final Block CITRIN_BLOCK = null;
	public static final Block HONEYCOMB = null;
	@ObjectHolder("honeycake") public static final Block HONEY_CAKE = null;
	public static final Block STRAWBERRY_CROP = null;
	@ObjectHolder("chocolatecake") public static final Block CHOCOLATE_CAKE = null;
	@ObjectHolder("strawberrycake") public static final Block STRAWBERRY_CAKE = null;
	@ObjectHolder("rhubarb") public static final Block RHUBARB_PLANT = null;
	@ObjectHolder("creepercake") public static final Block CREEPER_CAKE = null;
	public static final Block FROZEN_STONE = null;
	public static final Block FROZEN_BRICK = null;
	public static final Block FROZEN_GLOWSTONE = null;
	public static final Block BAMBUS_CROP = null;
	@ObjectHolder("ever_ice") public static final Block EVER_ICE_BLOCK = null;
	@ObjectHolder("rhubarbpie") public static final Block RHUBARB_PIE = null;
	public static final Block LITTLE_BUSH = null;
	public static final Block OPAQUE_BUSH = null;
	public static final Block BAMBUS_BLOCK = null;
	public static final Block EFFECT_BUSH = null;
	@ObjectHolder("keylock") public static final Block KEY_LOCK = null;
	public static final Block ICE_ALTAR = null;
	public static final Block PLATINUM_ORE = null;
	public static final Block PLATINUM_BLOCK = null;
	@ObjectHolder("mina_planks") public static final Block PLANKS = null;
	public static final Block LOG_PEPPEL = null;
	public static final Block LOG_COTTON = null;
	public static final Block LOG_CHESTNUT = null;
	public static final Block LOG_CHERRY = null;
	public static final Block LOG_PALM = null;
	public static final BlockMinaLeaf MINA_LEAVES_A = null;
	//public static final BlockMinaLeaf MINA_LEAVES_B = (BlockMinaLeaf) new BlockMinaLeaf.B().setUnlocalizedName("mina_leaves_b");
	public static final Block PALM_LEAVES = null;
	@ObjectHolder("mina_sapling") public static final Block SAPLING = null;
	@ObjectHolder("mina_wooden_slab") public static final BlockSlab WOODEN_SLAB = null;
	@ObjectHolder("mina_double_wooden_slab") public static final BlockSlab DOUBLE_WOODEN_SLAB = null;
	public static final Block PEPPEL_STAIRS = null;
	public static final Block COTTON_STAIRS = null;
	public static final Block CHESTNUT_STAIRS = null;
	public static final Block CHERRY_STAIRS = null;
	public static final Block PALM_STAIRS = null;
	
	@ObjectHolder("letterbox_oak") public static final Block OAK_LETTERBOX = null;
	@ObjectHolder("letterbox_spruce") public static final Block SPRUCE_LETTERBOX = null;
	@ObjectHolder("letterbox_birch") public static final Block BIRCH_LETTERBOX = null;
	@ObjectHolder("letterbox_jungle") public static final Block JUNGLE_LETTERBOX = null;
	@ObjectHolder("letterbox_acacia") public static final Block ACACIA_LETTERBOX = null;
	@ObjectHolder("letterbox_big_oak") public static final Block BIG_OAK_LETTERBOX = null;
	@ObjectHolder("letterbox_chestnut") public static final Block CHESTNUT_LETTERBOX = null;
	@ObjectHolder("letterbox_cherry") public static final Block CHERRY_LETTERBOX = null;
	@ObjectHolder("letterbox_peppel") public static final Block PEPPEL_LETTERBOX = null;
	@ObjectHolder("letterbox_cotton") public static final Block COTTON_LETTERBOX = null;
	@ObjectHolder("letterbox_palm") public static final Block PALM_LETTERBOX = null;
	@ObjectHolder("letterbox_bambus") public static final Block BAMBUS_LETTERBOX = null;
	
	public static final Block SAPPHIRE_ORE = null;
	public static final Block RUBY_ORE = null;
	public static final Block SAPPHIRE_BLOCK = null;
	public static final Block RUBY_BLOCK = null;
	public static final Block ELEVATOR_FLOOR = null;
	public static final Block ELEVATOR_CONTROL = null;
	public static final Block FROZEN_BRICK_STAIRS = null;
	@ObjectHolder("autofeeder") public static final Block AUTO_FEEDER = null;
	public static final Block REDSTONE_CROSSROAD = null;
	public static final BlockCable CABLE = null;
	public static final Block WALL_CABLE = null;
	@ObjectHolder("milky_glass") public static final BlockCustomStainedGlass MILKY_GLASS = null;
	public static final Block LIT_MILKY_GLASS = null;
	public static final Block GIFT_BOX = null;
	public static final Block DOGE_CROP = null;
	public static final Block KEVIKUS_CROP = null;
	public static final Block TRACIUS_CROP = null;
	public static final Block CHILI_CROP = null;
	@ObjectHolder("cobblebrick_stone") public static final Block COBBLEBRICKS = null;
	public static final Block SLOT_MACHINE = null;
	public static final Block COLLECTOR = null;
	public static final Block CAMPFIRE = null;
	public static final Block OAK_CHAIR = null;
	public static final Block SIEVE = null;
	@ObjectHolder("herb") public static final Block HERB_CROP = null;
	public static final Block CAMPBENCH = null;
	public static final Block QUICKSAND = null;
	public static final Block COCONUT = null;
	@ObjectHolder("dimmable_lamp") public static final Block DIMMABLE_LIGHT = null;
	public static final Block WABE_BLOCK = null;
	public static final Block FILTER = null;
	@ObjectHolder("autocrafter") public static final Block AUTO_CRAFTER = null;
	public static final Block RARE_EARTH_ORE = null;
	public static final Block PUMPKIN_LANTERN = null;
	public static final Block SOLAR_PANEL = null;
	@ObjectHolder("etr_converter") public static final Block ENERGY_TO_REDSTONE = null;
	public static final Block ACCUMULATOR = null;
	public static final Block OVEN = null;
	public static final Block COPPER_ORE = null;
    public static final Block COPPER_BLOCK = null;
    public static final Block RUBBER_TREE = null;
    public static final Block GENERATOR = null;
	@ObjectHolder("coal_compressor") public static final Block COMPRESSOR = null;
    public static final Block GRAVESTONE = null;
    public static final Block BOOK = null;
    
    private static void registerTable(IRegistrationInterface<Block, BlockRegistrationHandler> registry, BlockTable.TableType type, String name) {
    	registry.register(new BlockTable(BlockTable.TableType.Wood), name).autoname().blockOnly().submit();
    }
	
	static void registerBlocks(IRegistrationInterface<Block, BlockRegistrationHandler> registry) {
		registerTable(registry, BlockTable.TableType.Wood, "oak_table");
		registerTable(registry, BlockTable.TableType.Wood, "spruce_table");
		registerTable(registry, BlockTable.TableType.Wood, "birch_table");
		registerTable(registry, BlockTable.TableType.Wood, "jungle_table");
		registerTable(registry, BlockTable.TableType.Wood, "acacia_table");
		registerTable(registry, BlockTable.TableType.Wood, "dark_oak_table");
		registerTable(registry, BlockTable.TableType.Wood, "peppel_table");
		registerTable(registry, BlockTable.TableType.Wood, "chestnut_table");
		registerTable(registry, BlockTable.TableType.Wood, "cherry_table");
		registerTable(registry, BlockTable.TableType.Wood, "cotton_table");
		registerTable(registry, BlockTable.TableType.Stone, "stone_table");
		registerTable(registry, BlockTable.TableType.Rare, "iron_table");
		registerTable(registry, BlockTable.TableType.Rare, "gold_table");
		registerTable(registry, BlockTable.TableType.Rare, "lapis_table");
		registerTable(registry, BlockTable.TableType.Rare, "obsidian_table");
		registerTable(registry, BlockTable.TableType.Rare, "diamond_table");
		registerTable(registry, BlockTable.TableType.Rare, "ice_table");
		registerTable(registry, BlockTable.TableType.Rare, "citrin_table");
		registerTable(registry, BlockTable.TableType.Rare, "sponge_table");
		registerTable(registry, BlockTable.TableType.Rare, "quartz_table");
		registerTable(registry, BlockTable.TableType.Rare, "emerald_table");

		registry.register(new BlockStool().setUnlocalizedName("stool"), "stool_oak").blockOnly().submit();
		registry.register(new BlockStool().setUnlocalizedName("stool"), "stool_dark_oak").blockOnly().submit();
		registry.register(new BlockStool().setUnlocalizedName("stool"), "stool_acacia").blockOnly().submit();

		registry.register(new BlockNamieFlower(), "namie_flower_crop").blockOnly().submit();
		registry.register(new BlockCustomOre(2), "citrin_ore").autoname().ore("oreCitrin").submit();
		registry.register(new BlockCustom(Material.IRON).setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "citrin_block").autoname().ore("blockCitrin").submit();
		registry.register(new BlockHoneycomb(), "honeycomb").autoname().ore("honeycomb").submit();
		registry.register(new BlockCustomCake(), "honeycake").autoname().submit();
		registry.register(new BlockStrawberryCrop(), "strawberry_crop").blockOnly().submit();
		registry.register(new BlockCustomCake(), "strawberrycake").autoname().submit();
		registry.register(new BlockCustomCake(), "chocolatecake").autoname().submit();
		registry.register(new BlockRhubarb(), "rhubarb").blockOnly().submit();
		registry.register(new BlockCustomCake().setFoodAmountPerSlice(4), "creepercake").submit();
		registry.register(new BlockCustom(Material.ROCK).setHardness(1.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "frozen_stone").autoname().submit();
		registry.register(new BlockCustom(Material.GLASS).setSoundType(SoundType.GLASS).setHardness(0.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setLightLevel(1f), "frozen_glowstone").autoname().submit();

		final Block frozenBrick = new BlockCustom(Material.ROCK).setHardness(3f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		registry.register(frozenBrick, "frozen_brick").autoname().submit();
		registry.register(new BlockCustomStairs(frozenBrick.getDefaultState()).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "frozen_brick_stairs").autoname().submit();
		
		registry.register(new BlockBambusCrop(), "bambus_crop").blockOnly().submit();
		registry.register(new BlockCustom(Material.ROCK).setSoundType(SoundType.GLASS).setHardness(8f).setResistance(11f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "ever_ice").autoname().submit();
		registry.register(new BlockCustomCake().setBlockHeight(4f / 16f), "rhubarbpie").autoname().submit();
		registry.register(new BlockCustomBush().setCreativeTab(CreativeTabs.DECORATIONS), "little_bush").autoname().submit();
		registry.register(new BlockCustomBush().setCreativeTab(CreativeTabs.DECORATIONS), "opaque_bush").autoname().submit();
		registry.register(new BlockCustomAxis(Material.CLOTH, MapColor.GRASS).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "bambus_block").autoname().submit();
		registry.register(new BlockEffectBush(), "effect_bush").blockOnly().submit();
		registry.register(new BlockLock(), "keylock").autoname().submit();
		if(FeatureList.enable_ice_altar){
			registry.register(new BlockIceAltar().setCreativeTab(CreativeTabs.DECORATIONS), "ice_altar").autoname().submit();
		}
		registry.register(new BlockCustomOre(2, 4.5f, 6.0f), "platinum_ore").autoname().ore("orePlatinum").submit();
		registry.register(new BlockCustom(Material.IRON).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(3.5f).setResistance(6f), "platinum_block").autoname().ore("blockPlatinum").submit();
		registry.register(new BlockStandaloneLog(BlockMinaPlanks.EnumType.PEPPEL), "log_peppel").autoname().submit();
		registry.register(new BlockStandaloneLog(BlockMinaPlanks.EnumType.COTTON), "log_cotton").autoname().submit();
		registry.register(new BlockStandaloneLog(BlockMinaPlanks.EnumType.CHESTNUT), "log_chestnut").autoname().submit();
		registry.register(new BlockStandaloneLog(BlockMinaPlanks.EnumType.CHERRY), "log_cherry").autoname().submit();
		registry.register(new BlockMinaLeaf.A(), "mina_leaves_a").autoname().blockOnly().submit();
		//proxy.registerBlockOnly(MINA_LEAVES_B, "mina_leaves_b");
		registry.register(new BlockPalmLeaves(), "palm_leaves").autoname().blockOnly().submit();
		registry.register(new BlockMinaSapling(), "mina_sapling").autoname().blockOnly().submit();
		registry.register(new BlockHerb(), "herb").blockOnly().submit();
		
		registry.register(new BlockLetterbox(), "letterbox_oak").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_spruce").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_birch").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_jungle").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_acacia").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_big_oak").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_chestnut").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_cherry").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_peppel").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_cotton").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_palm").autoname().submit();
		registry.register(new BlockLetterbox(), "letterbox_bambus").autoname().submit();
		registry.register(new BlockCustomOre(), "ruby_ore").autoname().ore("oreRuby").submit();
		registry.register(new BlockCustomOre(), "sapphire_ore").autoname().ore("oreSapphire").submit();
		registry.register(new BlockCustom(Material.IRON).setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "ruby_block").autoname().ore("blockRuby").submit();
		registry.register(new BlockCustom(Material.IRON).setHardness(3.5F).setResistance(5.5F).setLightLevel(2.5f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS), "sapphire_block").autoname().ore("blockSapphire").submit();
		registry.register(new BlockElevatorFloor(), "elevator_floor").autoname().blockOnly().submit();
		registry.register(new BlockElevatorControl(), "elevator_control").autoname().submit();
		registry.register(new BlockAutoFeeder(), "autofeeder").autoname().submit();
		if(FeatureList.enable_redstone_crossroad){
			registry.register(new BlockRedstoneCrossroad(), "redstone_crossroad").autoname().submit();
		}
		registry.register(new BlockCustomStainedGlass().setHardness(0.3F), "milky_glass").autoname().blockOnly().submit();
		registry.register(new BlockCustomStainedGlass().setHardness(0.5f).setLightLevel(1f), "lit_milky_glass").autoname().blockOnly().submit();
		registry.register(new BlockGiftBox(), "gift_box").autoname().blockOnly().submit();
		registry.register(new BlockBerryCrop(BlockBerryCrop.BerryType.DOGE), "doge_crop").blockOnly().submit();
		registry.register(new BlockBerryCrop(BlockBerryCrop.BerryType.KEVIKUS), "kevikus_crop").blockOnly().submit();
		registry.register(new BlockBerryCrop(BlockBerryCrop.BerryType.TRACIUS), "tracius_crop").blockOnly().submit();
		registry.register(new BlockChiliCrop(), "chili_crop").blockOnly().submit();
		registry.register(new BlockCombined(Material.ROCK).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.DECORATIONS).setHardness(4.0F).setResistance(6.0F), "cobblebrick_stone").autoname().blockOnly().submit();
		registry.register(new BlockCollector(), "collector").autoname().submit();
		registry.register(new BlockCampfire(), "campfire").autoname().submit();
		registry.register(new BlockSlotMachine(), "slot_machine").autoname().submit();
		if(FeatureList.enable_chairs)registry.register(new BlockChair(), "oak_chair").autoname().submit();
		registry.register(new BlockSieve(), "sieve").autoname().submit();
		registry.register(new BlockCampbench(), "campbench").autoname().blockOnly().submit();
		registry.register(new BlockQuicksand(), "quicksand").autoname().submit();
		registry.register(new BlockPalmLog(BlockMinaPlanks.EnumType.PALM), "log_palm").autoname().submit();
		registry.register(new BlockMinaWoodSlab.Half().setUnlocalizedName("woodSlab"), "mina_wooden_slab").blockOnly().submit();
		registry.register(new BlockMinaWoodSlab.Double().setUnlocalizedName("woodSlab"), "mina_double_wooden_slab").blockOnly().submit();

		final Block planks = new BlockMinaPlanks();
		final IBlockState planksDefault = planks.getDefaultState();
		registry.register(planks, "mina_planks").blockOnly().submit();
		registry.register(new BlockCustomStairs(planksDefault.withProperty(BlockMinaPlanks.VARIANT, BlockMinaPlanks.EnumType.PEPPEL)), "peppel_stairs").autoname().submit();
		registry.register(new BlockCustomStairs(planksDefault.withProperty(BlockMinaPlanks.VARIANT, BlockMinaPlanks.EnumType.COTTON)), "cotton_stairs").autoname().submit();
		registry.register(new BlockCustomStairs(planksDefault.withProperty(BlockMinaPlanks.VARIANT, BlockMinaPlanks.EnumType.CHESTNUT)), "chestnut_stairs").autoname().submit();
		registry.register(new BlockCustomStairs(planksDefault.withProperty(BlockMinaPlanks.VARIANT, BlockMinaPlanks.EnumType.CHERRY)), "cherry_stairs").autoname().submit();
		registry.register(new BlockCustomStairs(planksDefault.withProperty(BlockMinaPlanks.VARIANT, BlockMinaPlanks.EnumType.PALM)), "palm_stairs").autoname().submit();
		
		registry.register(new BlockCoconut(), "coconut").autoname().ore("coconut").submit();
		registry.register(new BlockDimmableLight(), "dimmable_lamp").autoname().submit();
		registry.register(new BlockCustomRotatedPillar(Material.CLOTH, MapColor.BROWN).setCreativeTab(CreativeTabs.DECORATIONS), "wabe_block").autoname().submit();
		registry.register(new BlockFilter(), "filter").autoname().submit();
		registry.register(new BlockAutoCrafter(), "autocrafter").autoname().submit();
		registry.register(new BlockCustomOre(), "rare_earth_ore").blockOnly().submit();
		registry.register(new BlockPumpkinLantern(), "pumpkin_lantern").autoname().submit();
		registry.register(new BlockCable(), "cable").autoname().blockOnly().submit();
		registry.register(new BlockWallCable(), "wall_cable").autoname().blockOnly().submit();
		registry.register(new BlockSolarPanel(), "solar_panel").autoname().submit();
		registry.register(new BlockEnergyToRedstone(), "etr_converter").autoname().submit();
		registry.register(new BlockAccumulator(), "accumulator").autoname().submit();
		registry.register(new BlockOven(), "oven").autoname().submit();
		registry.register(new BlockCustomOre(1, 4.0f, 5.0f), "copper_ore").autoname().ore("oreCopper").submit();
		registry.register(new BlockCopperBlock(), "copper_block").autoname().ore("blockCopper").submit();
		registry.register(new BlockRubberTree(), "rubber_tree").blockOnly().submit();
		registry.register(new BlockGenerator(), "generator").autoname().submit();
		registry.register(new BlockCoalCompressor(), "coal_compressor").autoname().submit();
		registry.register(new BlockGravestone(), "gravestone").blockOnly().submit();
		registry.register(new BlockBook(), "book").blockOnly().submit();
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
	
	public static void verifyObjectHolders() throws IllegalArgumentException, IllegalAccessException {
		Field fields[] = MinaBlocks.class.getDeclaredFields();
		for(Field field : fields) {
			if(field.get(null) == null) {
				System.err.println("ObjectHolder MinaBlocks::" + field.getName() + " is null");
			}
		}
	}
}
