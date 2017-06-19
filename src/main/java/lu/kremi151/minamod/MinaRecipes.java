package lu.kremi151.minamod;

import java.lang.reflect.Field;

import lu.kremi151.minamod.annotations.AutoArmorRecipe;
import lu.kremi151.minamod.annotations.AutoToolRecipe;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemAmulet;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.recipe.RecipeAddToGiftBox;
import lu.kremi151.minamod.recipe.RecipeColoredBook;
import lu.kremi151.minamod.recipe.RecipeColoredBookCloning;
import lu.kremi151.minamod.recipe.RecipeColoredKey;
import lu.kremi151.minamod.recipe.RecipeCopyKey;
import lu.kremi151.minamod.recipe.RecipeDamagedSoulPearl;
import lu.kremi151.minamod.recipe.RecipeEncodeKey;
import lu.kremi151.minamod.recipe.RecipeGiftBox;
import lu.kremi151.minamod.recipe.RecipeHerbMixture;
import lu.kremi151.minamod.recipe.TMPRecipeHerbGuide;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

public class MinaRecipes {

	private static boolean init = false, init_brewing = false, init_furnace = false;

	static void initCraftingRecipes(MinaMod mod) {
		if (init)throw new RuntimeException("Duplicate call of function");
		initDefaultRecipes();
		initColoredRecipes();
		initStoolRecipes();
		initTableRecipes();
		initWoodRecipes();
		initHerbRecipes();
		initCustomRecipes();
		initArmorRecipes();
		init = true;
	}

	static void initFurnaceRecipes(MinaMod mod) {
		if (init_furnace)throw new RuntimeException("Duplicate call of function");
		// GameRegistry.registerFuelHandler(mod);
		// TODO: GameRegistry.addSmelting(MinaItems.itemHoneywabe, new
		// ItemStack(MinaItems.itemHoney,1), 0f);
		GameRegistry.addSmelting(MinaItems.CHESTNUT, new ItemStack(MinaItems.CHESTNUT_COOKED, 1), 0f);
		GameRegistry.addSmelting(MinaBlocks.PLATINUM_ORE, new ItemStack(MinaItems.PLATINUM_INGOT), 1f);
		init_furnace = true;
	}

	static void initBrewingRecipes(MinaMod mod) {
		if (init_brewing)throw new RuntimeException("Duplicate call of function");
		// BrewingRecipeRegistry.addRecipe(new ItemStack(Items.potionitem, 1,
		// 64), new ItemStack(MinaItems.itemRhubarb), new
		// ItemStack(MinaItems.itemAquaGem));
		init_brewing = true;
	}

	private static void initDefaultRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CITRIN_BLOCK, 1), "CCC", "CCC", "CCC", 'C',
				MinaItems.CITRIN);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.CITRIN, 9), MinaBlocks.CITRIN_BLOCK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.BAMBUS_BLOCK, 1), "BBB", "BBB", "BBB", 'B',
				MinaItems.BAMBUS);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.BAMBUS, 9), MinaBlocks.BAMBUS_BLOCK);
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.OBSIDIAN, 1), "OO", "OO", 'O',
				MinaItems.OBSIDIAN_FRAGMENT);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.PLATINUM_BLOCK, 1), "III", "III", "III", 'I',
				MinaItems.PLATINUM_INGOT);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.PLATINUM_INGOT, 9), MinaBlocks.PLATINUM_BLOCK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.SAPPHIRE_BLOCK, 1), "CCC", "CCC", "CCC", 'C',
				MinaItems.SAPPHIRE);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.SAPPHIRE, 9), MinaBlocks.SAPPHIRE_BLOCK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.RUBY_BLOCK, 1), "CCC", "CCC", "CCC", 'C',
				MinaItems.RUBY);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.RUBY, 9), MinaBlocks.RUBY_BLOCK);

		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.HONEY_CAKE, 1), "MMM", "HEH", "WHW", 'M',
				Items.MILK_BUCKET, 'H', MinaItems.HONEY_POT, 'E', Items.EGG, 'W', Items.WHEAT);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.STRAWBERRY_CAKE, 1), "MMM", "SES", "WSW", 'M',
				Items.MILK_BUCKET, 'S', MinaItems.STRAWBERRY, 'E', Items.EGG, 'W', Items.WHEAT);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CHOCOLATE_CAKE, 1), "MMM", "CEC", "WCW", 'M',
				Items.MILK_BUCKET, 'C', new ItemStack(Items.DYE, 1, 3), 'E', Items.EGG, 'W', Items.WHEAT);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.RHUBARB_PIE, 1), "MMM", "RER", "WRW", 'M',
				Items.MILK_BUCKET, 'R', MinaItems.RHUBARB, 'E', Items.EGG, 'W', Items.WHEAT);

		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.FROZEN_BRICK, 1), "FF", "FF", 'F',
				MinaBlocks.FROZEN_STONE);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.EVER_ICE_BLOCK, 1), "FF", "FF", 'F',
				MinaItems.EVER_SNOW);

		if(FeatureList.enable_soul_pearls)GameRegistry.addShapedRecipe(new ItemStack(MinaItems.SOUL_PEARL, 1), " C ", "CSC", " C ", 'C',
				MinaItems.CITRIN, 'S', Blocks.SOUL_SAND);

		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.CHIP, 1, 0), Items.GOLD_NUGGET, Items.REDSTONE);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.CHIP, 1, 1), Items.GOLD_INGOT, Items.REDSTONE,
				Items.IRON_INGOT);
		
		GameRegistry.addRecipe(ItemKey.rawKey(), " L", "I ", 'I', Items.IRON_INGOT, 'L',
				Items.LEATHER);
		GameRegistry.addRecipe(new ItemStack(MinaBlocks.KEY_LOCK, 1), "IGI", "CRC", "IGI", 'I', Items.IRON_INGOT,
				'G', Items.GOLD_INGOT, 'C', Blocks.COBBLESTONE, 'R', Items.REDSTONE);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.CHILI_POWDER), MinaItems.CHILI);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.SWEET_SOUR), MinaItems.CHILI_POWDER,
				Items.WATER_BUCKET, Items.SUGAR, Items.BOWL);
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.POULET_SPECIAL_PIQUANT), MinaItems.SWEET_SOUR,
				Items.COOKED_CHICKEN);

		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.GOLDEN_COIN, 1), "GG", "GG", 'G', Items.GOLD_NUGGET);

		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.NAMIE_SEEDS, 4), MinaItems.NAMIE_FRUIT);

		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.EMPTY_POT, 1), "A A", "A A", "AAA", 'A', Items.BRICK);

		if(FeatureList.enable_plate){
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.PLATE, 1), "A A", "AAA", 'A', Items.QUARTZ);
		}

		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ELEVATOR_CONTROL, 1), "III", "QCQ", 'I',
				Items.IRON_INGOT, 'Q', Items.QUARTZ, 'C', new ItemStack(MinaItems.CHIP, 1, 1));
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ELEVATOR_FLOOR, 1, 0), "IQI", "QIQ", "IQI", 'I',
				Items.IRON_INGOT, 'Q', Items.QUARTZ);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ELEVATOR_FLOOR, 1, 1), "IQI", "QSQ", "IQI", 'I',
				Items.IRON_INGOT, 'Q', Items.QUARTZ, 'S', Items.SLIME_BALL);
		
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.IRON_SICKLE, 1), "II ", " IH", "  H", 'I', Items.IRON_INGOT, 'H', Items.STICK);

		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.BERRY_SEEDS, 2, 0), new ItemStack(MinaItems.BERRY, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.BERRY_SEEDS, 2, 1), new ItemStack(MinaItems.BERRY, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.BERRY_SEEDS, 2, 2), new ItemStack(MinaItems.BERRY, 1, 2));
		
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.PUMPKIN_SOUP), Items.BOWL, Blocks.PUMPKIN, Items.PUMPKIN_SEEDS);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.CANDY_CANE, 1), " S ", "SRS", "  S", 'S', Items.SUGAR, 'R', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.COBBLEBRICKS, 4), "CC", "CC", 'C', Blocks.COBBLESTONE);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.ICE_SWORD, 1), "I", "I", "B", 'I', Blocks.ICE, 'B', Items.BLAZE_ROD);
	
		GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.GOULASH, 1), Items.BAKED_POTATO, Items.MUSHROOM_STEW, Items.CARROT, Items.BEEF);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.KATANA, 1), "  E", "WE ", "BW ", 'B', MinaItems.BAMBUS, 'E', Items.IRON_INGOT, 'W', Blocks.WOOL);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.COIN_BAG, 1), "L", "L", 'L', Items.LEATHER);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.KEY, 1), " L", "I ", 'L', Items.LEATHER, 'I', Items.IRON_INGOT);
	
		//Amulet of experience:
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.AMULET, 1, 0), " S ", "S S", " N ", 'S', Items.STRING, 'N', Items.NETHER_STAR);
		//Amulet of ender:
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.AMULET, 1, 1), " S ", "S S", " E ", 'S', Items.STRING, 'E', Items.ENDER_EYE);
		//Amulet of return:
		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.AMULET, 1, 2), " S ", "SES", " Y ", 'S', Items.STRING, 'Y', Items.ENDER_EYE, 'E', Items.EMERALD);
		
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.COLLECTOR, 1), "CCC", "WHW", "CRC", 'C', Blocks.COBBLESTONE, 'W', Blocks.WOOL, 'H', Blocks.HOPPER, 'R', Items.REDSTONE);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CAMPFIRE, 1), " S ", "SCS", "SSS", 'S', Items.STICK, 'C', new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE));
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.NAME_TAG, 2), "P ", " G", 'P', MinaItems.PLATINUM_INGOT, 'G', Items.GOLD_INGOT);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.SIEVE, 1), "I I", "IFI", "I I", 'I', Items.IRON_INGOT, 'F', Items.STRING);
	
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.COBBLEBRICKS, 6, 1), "CCC", 'C', new ItemStack(MinaBlocks.COBBLEBRICKS, 1, 0));

		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CAMPBENCH, 2, 0), "WW", "SS", 'W', new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.ACACIA.getMetadata() % 4), 'S', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CAMPBENCH, 2, 1), "WW", "SS", 'W', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.getMetadata() % 4), 'S', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CAMPBENCH, 2, 2), "WW", "SS", 'W', new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() % 4), 'S', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CAMPBENCH, 2, 3), "WW", "SS", 'W', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.getMetadata() % 4), 'S', Items.STICK);

		GameRegistry.addShapedRecipe(new ItemStack(MinaItems.HERB_GUIDE, 1), "HBH", 'B', Items.WRITABLE_BOOK, 'H', new ItemStack(MinaItems.HERB, 1, OreDictionary.WILDCARD_VALUE));
	
		GameRegistry.addShapelessRecipe(new ItemStack(MinaBlocks.QUICKSAND, 1), Items.WATER_BUCKET, Blocks.SAND);
	}
	
	private static void initColoredRecipes(){
		if(init)throw new RuntimeException("Duplicate call of function");
		for(int i = 0 ; i < EnumDyeColor.values().length ; i++){
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.MILKY_GLASS, 8, i), "AAA", "ABA", "AAA", 'A', new ItemStack(Blocks.STAINED_GLASS, 1, i), 'B', Items.MILK_BUCKET);
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.LIT_MILKY_GLASS, 8, i), "AAA", "ABA", "AAA", 'A', new ItemStack(MinaBlocks.MILKY_GLASS, 1, i), 'B', MinaBlocks.FROZEN_GLOWSTONE);
		}
	}

	private static void initStoolRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		for (int i = 0; i < 16; i++) {
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ACACIA_STOOL, 3, i), "AA", "BB", 'A',
					new ItemStack(Blocks.WOOL, 1, i), 'B', new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.ACACIA.getMetadata()));
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.DARK_OAK_STOOL, 3, i), "AA", "BB", 'A',
					new ItemStack(Blocks.WOOL, 1, i), 'B', new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()));
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.OAK_STOOL, 3, i), "AA", "BB", 'A',
					new ItemStack(Blocks.WOOL, 1, i), 'B', new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()));
		}
	}

	private static void initTableRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.OAK_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()), 'B', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.SPRUCE_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()), 'B', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.BIRCH_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), 'B', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.JUNGLE_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()), 'B', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ACACIA_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.ACACIA.getMetadata()), 'B', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.DARK_OAK_TABLE, 1), "AAA", " B ", " A ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()), 'B', Items.STICK);
		//TODO: Trees
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockChestnutTable, 1), "AAA", " B ", " A ", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.CHESTNUT.getMetadata()), 'B',
//				Items.STICK);
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockCherryTable, 1), "AAA", " B ", " A ", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.CHERRY.getMetadata()), 'B',
//				Items.STICK);
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockKremiTable, 1), "AAA", " B ", " A ", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.KREMI.getMetadata()), 'B',
//				Items.STICK);
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockPeppelTable, 1), "AAA", " B ", " A ", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.PEPPEL.getMetadata()), 'B',
//				Items.STICK);
	}

	private static void initWoodRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.OAK_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.SPRUCE_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.BIRCH_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.JUNGLE_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.ACACIA_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.ACACIA.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.BIG_OAK_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()), 'B', Blocks.HOPPER, 'C',
				Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CHESTNUT_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(MinaBlocks.PLANKS, 1, BlockMinaPlanks.EnumType.CHESTNUT.getMetadata()), 'B',
				Blocks.HOPPER, 'C', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.CHERRY_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(MinaBlocks.PLANKS, 1, BlockMinaPlanks.EnumType.CHERRY.getMetadata()), 'B',
				Blocks.HOPPER, 'C', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.PEPPEL_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(MinaBlocks.PLANKS, 1, BlockMinaPlanks.EnumType.PEPPEL.getMetadata()), 'B',
				Blocks.HOPPER, 'C', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.COTTON_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				new ItemStack(MinaBlocks.PLANKS, 1, BlockMinaPlanks.EnumType.COTTON.getMetadata()), 'B',
				Blocks.HOPPER, 'C', Items.STICK);
		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.BAMBUS_LETTERBOX, 1), " A ", "ABA", " C ", 'A',
				MinaBlocks.BAMBUS_BLOCK, 'B',
				Blocks.HOPPER, 'C', Items.STICK);
		
		for(BlockMinaPlanks.EnumType type : BlockMinaPlanks.EnumType.values()){
			GameRegistry.addShapelessRecipe(new ItemStack(Items.COAL, 1, 1), BlockStandaloneLog.getBlockFor(type));
			GameRegistry.addShapelessRecipe(new ItemStack(MinaBlocks.PLANKS, 4, type.getMetadata()), BlockStandaloneLog.getBlockFor(type));
			GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.WOODEN_SLAB, 6, type.getMetadata()), "PPP", 'P', new ItemStack(MinaBlocks.PLANKS, 1, type.getMetadata()));
		}

		//TODO: Trees
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockChestnutStairs, 4), "A  ", "AA ", "AAA", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.CHESTNUT.getMetadata()));
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockCherryStairs, 4), "A  ", "AA ", "AAA", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.CHERRY.getMetadata()));
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockPeppelStairs, 4), "A  ", "AA ", "AAA", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.PEPPEL.getMetadata()));
//		GameRegistry.addShapedRecipe(new ItemStack(MinaBlocks.blockKremiStairs, 4), "A  ", "AA ", "AAA", 'A',
//				new ItemStack(MinaBlocks.blockPlanks, 1, BlockMinaPlanks.EnumType.KREMI.getMetadata()));
	}

	private static void initHerbRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		for (EnumHerb herb : EnumHerb.values()) {
			GameRegistry.addShapelessRecipe(new ItemStack(MinaItems.POWDER, 1, herb.getHerbId()),
					new ItemStack(MinaItems.HERB, 1, herb.getHerbId()));
		}
	}

	private static void initCustomRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		GameRegistry.addRecipe(new RecipeColoredKey());
		GameRegistry.addRecipe(new RecipeHerbMixture());
		if(FeatureList.enable_soul_pearls)GameRegistry.addRecipe(new RecipeDamagedSoulPearl());
		GameRegistry.addRecipe(new RecipeColoredBook());
		GameRegistry.addRecipe(new RecipeColoredBookCloning());
		GameRegistry.addRecipe(new RecipeGiftBox());
		GameRegistry.addRecipe(new RecipeAddToGiftBox());
		GameRegistry.addRecipe(new RecipeCopyKey());
		GameRegistry.addRecipe(new RecipeEncodeKey());

		RecipeSorter.register(MinaMod.MODID + ":colored_key", RecipeColoredKey.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":herb_mixture", RecipeHerbMixture.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":damaged_soul_pearl", RecipeDamagedSoulPearl.class,
				RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":colored_book", RecipeColoredBook.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":colored_book_cloning", RecipeColoredBookCloning.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":copy_key", RecipeCopyKey.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":encode_key", RecipeEncodeKey.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register(MinaMod.MODID + ":gift_box_adding", RecipeAddToGiftBox.class, RecipeSorter.Category.SHAPELESS,
				"after:minecraft:shapeless");

//		RecipeSorter.register(MinaMod.MODID + ":herb_guidexxx", TMPRecipeHerbGuide.class, RecipeSorter.Category.SHAPELESS,
//				"after:minecraft:shapeless");//TODO: Fix & remove
		
		RecipeSorter.register(MinaMod.MODID + ":gift_box_packing", RecipeGiftBox.class, RecipeSorter.Category.SHAPED,
				"after:minecraft:shaped before:minecraft:shapeless");
		
	}
	
	private static final String TOOL_ARMOR_ITEM_MISSING = "Cannot find %s item for %s recipe \"%s\"";

	private static void initArmorRecipes() {
		if (init)throw new RuntimeException("Duplicate call of function");
		Field fields[] = MinaItems.class.getDeclaredFields();
		for(Field f : fields){
			f.setAccessible(true);
			AutoArmorRecipe armorNode = f.getAnnotation(AutoArmorRecipe.class);
			AutoToolRecipe toolNode = f.getAnnotation(AutoToolRecipe.class);
			if(armorNode != null && f.getType().isAssignableFrom(Item.class)){
				try {
					Item item = (Item) f.get(null);
					Item helmet = MinaUtils.findItem(MinaMod.MODID, (armorNode.helmet().length()>0)?armorNode.helmet():(armorNode.value() + "_helmet"));
					Item chestplate = MinaUtils.findItem(MinaMod.MODID, (armorNode.chestplate().length()>0)?armorNode.chestplate():(armorNode.value() + "_chestplate"));
					Item leggings = MinaUtils.findItem(MinaMod.MODID, (armorNode.leggings().length()>0)?armorNode.leggings():(armorNode.value() + "_leggings"));
					Item boots = MinaUtils.findItem(MinaMod.MODID, (armorNode.boots().length()>0)?armorNode.boots():(armorNode.value() + "_boots"));
					
					if(helmet != null){
						GameRegistry.addShapedRecipe(new ItemStack(helmet, 1), "AAA", "A A", 'A', item);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "helmet", "armor", armorNode.value());
					}
					if(chestplate != null){
						GameRegistry.addShapedRecipe(new ItemStack(chestplate, 1), "A A", "AAA", "AAA", 'A', item);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "chestplate", "armor", armorNode.value());
					}
					if(leggings != null){
						GameRegistry.addShapedRecipe(new ItemStack(leggings, 1), "AAA", "A A", "A A", 'A', item);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "leggings", "armor", armorNode.value());
					}
					if(boots != null){
						GameRegistry.addShapedRecipe(new ItemStack(boots, 1), "A A", "A A", 'A', item);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "boots", "armor", armorNode.value());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if(toolNode != null && f.getType().isAssignableFrom(Item.class)){
				try {
					Item item = (Item) f.get(null);
					Item sword = MinaUtils.findItem(MinaMod.MODID, (toolNode.sword().length()>0)?toolNode.sword():(toolNode.value() + "_sword"));
					Item axe = MinaUtils.findItem(MinaMod.MODID, (toolNode.axe().length()>0)?toolNode.axe():(toolNode.value() + "_axe"));
					Item pickaxe = MinaUtils.findItem(MinaMod.MODID, (toolNode.pickaxe().length()>0)?toolNode.pickaxe():(toolNode.value() + "_pickaxe"));
					Item shovel = MinaUtils.findItem(MinaMod.MODID, (toolNode.shovel().length()>0)?toolNode.shovel():(toolNode.value() + "_shovel"));
					Item hoe = MinaUtils.findItem(MinaMod.MODID, (toolNode.hoe().length()>0)?toolNode.hoe():(toolNode.value() + "_hoe"));
					
					if(sword != null){
						GameRegistry.addShapedRecipe(new ItemStack(sword, 1), "A", "A", "B", 'A', item, 'B', Items.STICK);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "sword", "tool", armorNode.value());
					}
					if(axe != null){
						GameRegistry.addShapedRecipe(new ItemStack(axe, 1), "AA", "AB", " B", 'A', item, 'B', Items.STICK);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "axe", "tool", armorNode.value());
					}
					if(pickaxe != null){
						GameRegistry.addShapedRecipe(new ItemStack(pickaxe, 1), "AAA", " B ", " B ", 'A', item, 'B', Items.STICK);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "pickaxe", "tool", armorNode.value());
					}
					if(shovel != null){
						GameRegistry.addShapedRecipe(new ItemStack(shovel, 1), "A", "B", "B", 'A', item, 'B', Items.STICK);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "shovel", "tool", armorNode.value());
					}
					if(hoe != null){
						GameRegistry.addShapedRecipe(new ItemStack(hoe, 1), "AA", " B", " B", 'A', item, 'B', Items.STICK);
					}else{
						MinaMod.errorln(TOOL_ARMOR_ITEM_MISSING, "hoe", "tool", armorNode.value());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
