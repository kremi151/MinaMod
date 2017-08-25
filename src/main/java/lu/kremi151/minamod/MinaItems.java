package lu.kremi151.minamod;

import lu.kremi151.minamod.annotations.AutoArmorRecipe;
import lu.kremi151.minamod.annotations.AutoToolRecipe;
import lu.kremi151.minamod.block.BlockCampbench;
import lu.kremi151.minamod.block.BlockCustomStainedGlass;
import lu.kremi151.minamod.block.BlockElevatorFloor;
import lu.kremi151.minamod.block.BlockGiftBox;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.item.ItemAmulet;
import lu.kremi151.minamod.item.ItemBambus;
import lu.kremi151.minamod.item.ItemBattery;
import lu.kremi151.minamod.item.ItemBerry;
import lu.kremi151.minamod.item.ItemBerrySeed;
import lu.kremi151.minamod.item.ItemChili;
import lu.kremi151.minamod.item.ItemChiliPowder;
import lu.kremi151.minamod.item.ItemChip;
import lu.kremi151.minamod.item.ItemCoinBag;
import lu.kremi151.minamod.item.ItemColoredWrittenBook;
import lu.kremi151.minamod.item.ItemCustomArmor;
import lu.kremi151.minamod.item.ItemCustomAxe;
import lu.kremi151.minamod.item.ItemCustomHoe;
import lu.kremi151.minamod.item.ItemCustomPickaxe;
import lu.kremi151.minamod.item.ItemCustomShovel;
import lu.kremi151.minamod.item.ItemCustomSword;
import lu.kremi151.minamod.item.ItemEmptyPot;
import lu.kremi151.minamod.item.ItemFoodContainer;
import lu.kremi151.minamod.item.ItemGoldenCoin;
import lu.kremi151.minamod.item.ItemHerb;
import lu.kremi151.minamod.item.ItemHerbGuide;
import lu.kremi151.minamod.item.ItemHerbMixture;
import lu.kremi151.minamod.item.ItemHoneyPot;
import lu.kremi151.minamod.item.ItemKatana;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemMilkBottle;
import lu.kremi151.minamod.item.ItemPowder;
import lu.kremi151.minamod.item.ItemSickle;
import lu.kremi151.minamod.item.ItemSoulPearl;
import lu.kremi151.minamod.item.ItemUnpackedGift;
import lu.kremi151.minamod.item.amulet.AmuletRegistry;
import lu.kremi151.minamod.item.block.ItemBlockCombined;
import lu.kremi151.minamod.item.block.ItemBlockGiftBox;
import lu.kremi151.minamod.item.block.ItemBlockLeaves;
import lu.kremi151.minamod.item.block.ItemBlockMulti;
import lu.kremi151.minamod.proxy.CommonProxy;
import lu.kremi151.minamod.util.FeatureList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class MinaItems {

	public static final Item EVER_SNOW = new Item().setUnlocalizedName("ever_snow")
			.setCreativeTab(CreativeTabs.MISC);
	public static final Item NAMIE_SEEDS = new ItemSeeds(MinaBlocks.NAMIE_FLOWER, Blocks.FARMLAND)
			.setUnlocalizedName("namie_seeds");
	public static final Item NAMIE_FRUIT = new Item().setUnlocalizedName("namie_fruit")
			.setCreativeTab(CreativeTabs.FOOD);
	public static final Item MILK_BOTTLE = new ItemMilkBottle().setUnlocalizedName("milk_bottle");
	@AutoToolRecipe(value="citrin", useOreName=true)
	@AutoArmorRecipe(value="citrin", useOreName=true)
	public static final Item CITRIN = new Item().setUnlocalizedName("citrin")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item CITRIN_SWORD = new ItemCustomSword(MinaToolMaterials.CITRIN)
			.setUnlocalizedName("citrin_sword");
	public static final Item CITRIN_AXE = new ItemCustomAxe(MinaToolMaterials.CITRIN)
			.setUnlocalizedName("citrin_axe");
	public static final Item CITRIN_PICKAXE = new ItemCustomPickaxe(MinaToolMaterials.CITRIN)
			.setUnlocalizedName("citrin_pickaxe");
	public static final Item CITRIN_SHOVEL = new ItemCustomShovel(MinaToolMaterials.CITRIN)
			.setUnlocalizedName("citrin_shovel");
	public static final Item CITRIN_HOE = new ItemCustomHoe(MinaToolMaterials.CITRIN)
			.setUnlocalizedName("citrin_hoe");
	public static final Item HONEYWABE = new Item().setUnlocalizedName("honeywabe")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item STRAWBERRY = new ItemSeedFood(1, 0.2f, MinaBlocks.STRAWBERRY_CROP, Blocks.FARMLAND).setUnlocalizedName("strawberry");
	@AutoToolRecipe("bambus")
	public static final Item BAMBUS = new ItemBambus().setUnlocalizedName("bambus");
	public static final Item BAMBUS_SWORD = new ItemCustomSword(MinaToolMaterials.BAMBUS)
			.setUnlocalizedName("bambus_sword");
	public static final Item BAMBUS_AXE = new ItemCustomAxe(MinaToolMaterials.BAMBUS)
			.setUnlocalizedName("bambus_axe");
	public static final Item BAMBUS_PICKAXE = new ItemCustomPickaxe(MinaToolMaterials.BAMBUS)
			.setUnlocalizedName("bambus_pickaxe");
	public static final Item BAMBUS_SHOVEL = new ItemCustomShovel(MinaToolMaterials.BAMBUS)
			.setUnlocalizedName("bambus_shovel");
	public static final Item BAMBUS_HOE = new ItemCustomHoe(MinaToolMaterials.BAMBUS)
			.setUnlocalizedName("bambus_hoe");
	public static final Item RHUBARB = new Item().setUnlocalizedName("rhubarb")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item CHESTNUT = new Item().setUnlocalizedName("chestnut")
			.setCreativeTab(CreativeTabs.FOOD);
	public static final Item CHESTNUT_COOKED = new ItemFood(4, 0f, false).setUnlocalizedName("chestnut_cooked")
			.setCreativeTab(CreativeTabs.FOOD);
	public static final Item CHERRY = new ItemFood(1, 0.2f, false).setUnlocalizedName("cherry");
	@AutoToolRecipe("obsidian")
	@AutoArmorRecipe("obsidian")
	public static final Item OBSIDIAN_FRAGMENT = new Item().setCreativeTab(CreativeTabs.MATERIALS)
			.setUnlocalizedName("obsidian_fragment");
	public static final Item OBSIDIAN_SWORD = new ItemCustomSword(MinaToolMaterials.OBSIDIAN)
			.setUnlocalizedName("obsidian_sword");
	public static final Item OBSIDIAN_AXE = new ItemCustomAxe(MinaToolMaterials.OBSIDIAN)
			.setUnlocalizedName("obsidian_axe");
	public static final Item OBSIDIAN_PICKAXE = new ItemCustomPickaxe(MinaToolMaterials.OBSIDIAN)
			.setUnlocalizedName("obsidian_pickaxe");
	public static final Item OBSIDIAN_SHOVEL = new ItemCustomShovel(MinaToolMaterials.OBSIDIAN)
			.setUnlocalizedName("obsidian_shovel");
	public static final Item OBSIDIAN_HOE = new ItemCustomHoe(MinaToolMaterials.OBSIDIAN)
			.setUnlocalizedName("obsidian_hoe");
	public static final Item CITRIN_HELMET = new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.HEAD)
			.setUnlocalizedName("citrin_helmet");
	public static final Item CITRIN_CHESTPLATE = new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.CHEST)
			.setUnlocalizedName("citrin_chestplate");
	public static final Item CITRIN_LEGGINGS = new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.LEGS)
			.setUnlocalizedName("citrin_leggings");
	public static final Item CITRIN_BOOTS = new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.FEET)
			.setUnlocalizedName("citrin_boots");
	public static final Item KEY = new ItemKey().setUnlocalizedName("key");
	public static final Item CHIP = new ItemChip().setUnlocalizedName("chip");
	public static final Item OBSIDIAN_HELMET = new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.HEAD)
			.setUnlocalizedName("obsidian_helmet");
	public static final Item OBSIDIAN_CHESTPLATE = new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.CHEST)
			.setUnlocalizedName("obsidian_chestplate");
	public static final Item OBSIDIAN_LEGGINGS = new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.LEGS)
			.setUnlocalizedName("obsidian_leggings");
	public static final Item OBSIDIAN_BOOTS = new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.FEET)
			.setUnlocalizedName("obsidian_boots");
	public static final Item BLACK_PEARL = new Item().setUnlocalizedName("black_pearl")
			.setCreativeTab(CreativeTabs.MISC);
	public static final Item WHITE_PEARL = new Item().setUnlocalizedName("white_pearl")
			.setCreativeTab(CreativeTabs.MISC);
	public static final Item HARMONY_PEARL = new Item().setUnlocalizedName("harmony_pearl")
			.setCreativeTab(CreativeTabs.MISC);
	public static final Item POULET_SPECIAL_PIQUANT = new ItemFoodContainer(14, 0.8F, false, Items.BOWL)
			.setUnlocalizedName("poulet_special_piquant").setCreativeTab(CreativeTabs.FOOD);
	public static final Item CHILI = new ItemChili().setUnlocalizedName("chili")
			.setCreativeTab(CreativeTabs.FOOD);
	public static final Item CHILI_POWDER = new ItemChiliPowder().setUnlocalizedName("chili_powder")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item SWEET_SOUR = new Item().setUnlocalizedName("sweet_sour")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final ItemGoldenCoin GOLDEN_COIN = (ItemGoldenCoin) new ItemGoldenCoin().setUnlocalizedName("golden_coin")
			.setCreativeTab(CreativeTabs.MATERIALS);
	@AutoArmorRecipe(value="platinum", useOreName=true)
	public static final Item PLATINUM_INGOT = new Item().setCreativeTab(CreativeTabs.MATERIALS)
			.setUnlocalizedName("platinum_ingot");
	public static final Item SOUL_PEARL = new ItemSoulPearl().setUnlocalizedName("soul_pearl");
	public static final Item PLATINUM_HELMET = new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.HEAD)
			.setUnlocalizedName("platinum_helmet");
	public static final Item PLATINUM_CHESTPLATE = new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.CHEST)
			.setUnlocalizedName("platinum_chestplate");
	public static final Item PLATINUM_LEGGINGS = new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.LEGS)
			.setUnlocalizedName("platinum_leggings");
	public static final Item PLATINUM_BOOTS = new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.FEET)
			.setUnlocalizedName("platinum_boots");
	public static final ItemHerb HERB = (ItemHerb) new ItemHerb().setUnlocalizedName("herb");
	public static final ItemPowder POWDER = (ItemPowder) new ItemPowder().setUnlocalizedName("powder");
	public static final Item EMPTY_POT = new ItemEmptyPot().setUnlocalizedName("empty_pot")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item HONEY_POT = new ItemHoneyPot().setUnlocalizedName("honey_pot")
			.setContainerItem(EMPTY_POT);
	public static final Item SAPPHIRE = new Item().setUnlocalizedName("sapphire")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item RUBY = new Item().setUnlocalizedName("ruby").setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item TURTLE_SHELL = new Item().setUnlocalizedName("turtle_shell")
			.setCreativeTab(CreativeTabs.MATERIALS);
	public static final Item TURTLE_HELMET = new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.HEAD)
			.setUnlocalizedName("turtle_shell_helmet");
	public static final Item TURTLE_CHESTPLATE = new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.CHEST)
			.setUnlocalizedName("turtle_shell_chestplate");
	public static final Item TURTLE_LEGGINGS = new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.LEGS)
			.setUnlocalizedName("turtle_shell_leggings");
	public static final Item TURTLE_BOOTS = new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.FEET)
			.setUnlocalizedName("turtle_shell_boots");
	public static final ItemHerbMixture MIXTURE = (ItemHerbMixture)new ItemHerbMixture().setUnlocalizedName("mixture");
	public static final ItemSickle IRON_SICKLE = (ItemSickle) new ItemSickle(ToolMaterial.IRON).setUnlocalizedName("iron_sickle");
	public static final ItemColoredWrittenBook COLORED_BOOK = (ItemColoredWrittenBook) new ItemColoredWrittenBook().setUnlocalizedName("written_book");
	public static final Item COTTON = new Item().setCreativeTab(CreativeTabs.MATERIALS).setUnlocalizedName("cotton");
	public static final Item BERRY = new ItemBerry(5, 1f, false).setCreativeTab(CreativeTabs.FOOD).setUnlocalizedName("berry");
	public static final Item BERRY_SEEDS = new ItemBerrySeed();
	public static final Item PUMPKIN_SOUP = new ItemSoup(10).setUnlocalizedName("pumpkin_soup");
	public static final ItemFood CANDY_CANE = (ItemFood) new ItemFood(2, 0.6f, false).setUnlocalizedName("candy_cane");
	public static final Item ICE_SWORD = new ItemSword(MinaToolMaterials.ICE).setUnlocalizedName("ice_sword");
	public static final ItemFood GOULASH = (ItemFood) new ItemSoup(12).setUnlocalizedName("goulash");
	public static final Item KATANA = new ItemKatana().setUnlocalizedName("katana");
	public static final Item COIN_BAG = new ItemCoinBag().setUnlocalizedName("coin_bag");
	public static final Item AMULET = new ItemAmulet().setUnlocalizedName("amulet");
	public static final ItemBattery BATTERY = (ItemBattery)new ItemBattery().setUnlocalizedName("battery");
	public static final Item HERB_GUIDE = new ItemHerbGuide().setUnlocalizedName("herb_guide");
	public static final Item CHICKEN_NUGGETS = new ItemFood(8, 0.6f, false).setUnlocalizedName("chicken_nuggets");
	public static final Item FLOUR = new Item().setCreativeTab(CreativeTabs.MATERIALS).setUnlocalizedName("flour");
	public static final ItemUnpackedGift UNPACKED_GIFT = (ItemUnpackedGift) new ItemUnpackedGift().setUnlocalizedName("unpacked_gift");
	
	private static boolean init = false, itemBInit = false;

	public static void registerItems() {
		if (init)throw new RuntimeException("Duplicate call of function");
		
		registerItemBlocks();
		
		CommonProxy proxy = MinaMod.getProxy();

		proxy.registerItem(EVER_SNOW, "ever_snow");
		proxy.registerItem(NAMIE_SEEDS, "namie_seeds");
		proxy.registerItem(NAMIE_FRUIT, "namie_fruit");
		proxy.registerItem(MILK_BOTTLE, "milk_bottle");
		proxy.registerItemWithOre(CITRIN, "citrin", "gemCitrin");
		proxy.registerItem(CITRIN_SWORD, "citrin_sword");
		proxy.registerItem(CITRIN_AXE, "citrin_axe");
		proxy.registerItem(CITRIN_PICKAXE, "citrin_pickaxe");
		proxy.registerItem(CITRIN_SHOVEL, "citrin_shovel");
		proxy.registerItem(CITRIN_HOE, "citrin_hoe");
		proxy.registerItem(HONEYWABE, "honeywabe");
		proxy.registerItemWithOre(STRAWBERRY, "strawberry");
		proxy.registerItem(BAMBUS, "bambus_item");
		proxy.registerItem(BAMBUS_SWORD, "bambus_sword");
		proxy.registerItem(BAMBUS_AXE, "bambus_axe");
		proxy.registerItem(BAMBUS_PICKAXE, "bambus_pickaxe");
		proxy.registerItem(BAMBUS_SHOVEL, "bambus_shovel");
		proxy.registerItem(BAMBUS_HOE, "bambus_hoe");
		proxy.registerItem(RHUBARB, "rhubarb_item");
		proxy.registerItemWithOre(CHESTNUT, "chestnut");
		proxy.registerItem(CHESTNUT_COOKED, "chestnut_cooked");
		proxy.registerItemWithOre(CHERRY, "cherry");
		proxy.registerItem(OBSIDIAN_FRAGMENT, "obsidian_fragment");
		proxy.registerItem(OBSIDIAN_SWORD, "obsidian_sword");
		proxy.registerItem(OBSIDIAN_AXE, "obsidian_axe");
		proxy.registerItem(OBSIDIAN_PICKAXE, "obsidian_pickaxe");
		proxy.registerItem(OBSIDIAN_SHOVEL, "obsidian_shovel");
		proxy.registerItem(OBSIDIAN_HOE, "obsidian_hoe");
		proxy.registerItem(CITRIN_HELMET, "citrin_helmet");
		proxy.registerItem(CITRIN_CHESTPLATE, "citrin_chestplate");
		proxy.registerItem(CITRIN_LEGGINGS, "citrin_leggings");
		proxy.registerItem(CITRIN_BOOTS, "citrin_boots");
		proxy.registerItem(KEY, "key");
		proxy.registerItem(CHIP, "chip", ItemChip.variant_names);
		proxy.registerItem(OBSIDIAN_HELMET, "obsidian_helmet");
		proxy.registerItem(OBSIDIAN_CHESTPLATE, "obsidian_chestplate");
		proxy.registerItem(OBSIDIAN_LEGGINGS, "obsidian_leggings");
		proxy.registerItem(OBSIDIAN_BOOTS, "obsidian_boots");
		if(FeatureList.enable_ice_altar){
			proxy.registerItem(BLACK_PEARL, "black_pearl");
			proxy.registerItem(WHITE_PEARL, "white_pearl");
			proxy.registerItem(HARMONY_PEARL, "harmony_pearl");
		}
		proxy.registerItem(POULET_SPECIAL_PIQUANT, "poulet_special_piquant");
		proxy.registerItemWithOre(CHILI, "chili");
		proxy.registerItem(CHILI_POWDER, "chili_powder");
		proxy.registerItem(SWEET_SOUR, "sweet_sour");
		proxy.registerItem(GOLDEN_COIN, "golden_coin", "golden_coin", "five_golden_coins");
		proxy.registerItemWithOre(PLATINUM_INGOT, "platinum_ingot", "ingotPlatinum");
		if(FeatureList.enable_soul_pearls)proxy.registerItem(SOUL_PEARL, "soul_pearl");
		proxy.registerItem(PLATINUM_HELMET, "platinum_helmet");
		proxy.registerItem(PLATINUM_CHESTPLATE, "platinum_chestplate");
		proxy.registerItem(PLATINUM_LEGGINGS, "platinum_leggings");
		proxy.registerItem(PLATINUM_BOOTS, "platinum_boots");
		proxy.registerItem(HERB, "herb");
		proxy.registerItem(POWDER, "powder");
		proxy.registerItem(EMPTY_POT, "empty_pot");
		proxy.registerItem(HONEY_POT, "honey_pot");
		proxy.registerItemWithOre(RUBY, "ruby", "gemRuby");
		proxy.registerItemWithOre(SAPPHIRE, "sapphire", "gemSapphire");
		proxy.registerItem(TURTLE_SHELL, "turtle_shell");
		proxy.registerItem(TURTLE_HELMET, "turtle_shell_helmet");
		proxy.registerItem(TURTLE_CHESTPLATE, "turtle_shell_chestplate");
		proxy.registerItem(TURTLE_LEGGINGS, "turtle_shell_leggings");
		proxy.registerItem(TURTLE_BOOTS, "turtle_shell_boots");
		proxy.registerItem(MIXTURE, "mixture");
		proxy.registerItem(IRON_SICKLE, "iron_sickle");
		proxy.registerItem(COLORED_BOOK, "colored_written_book");
		proxy.registerItemWithOre(COTTON, "cotton");
		proxy.registerItem(BERRY, "berry", ItemBerry.VARIANT_NAMES);
		proxy.registerItem(BERRY_SEEDS, "berry_seeds", "doge_seeds", "kevikus_seeds", "tracius_seeds");
		proxy.registerItem(PUMPKIN_SOUP, "pumpkin_soup");
		proxy.registerItem(CANDY_CANE, "candy_cane");
		proxy.registerItem(ICE_SWORD, "ice_sword");
		proxy.registerItem(GOULASH, "goulash");
		proxy.registerItem(KATANA, "katana");
		proxy.registerItem(COIN_BAG, "coin_bag");
		proxy.registerItem(AMULET, "amulet", AmuletRegistry.getVariantNames());
		proxy.registerItem(BATTERY, "battery");
		GameRegistry.register(HERB_GUIDE.setRegistryName(MinaMod.MODID, "herb_guide"));
		proxy.registerItem(CHICKEN_NUGGETS, "chicken_nuggets");
		proxy.registerItemWithOre(FLOUR, "flour", "flour");
		proxy.registerItem(UNPACKED_GIFT, "unpacked_gift", BlockGiftBox.getVariantNames("unpacked_gift"));

		init = true;
	}
	
	private static void registerItemBlocks(){
		if (itemBInit)throw new RuntimeException("Duplicate call of function");
		CommonProxy proxy = MinaMod.getProxy();
		
		proxy.registerItem(new ItemBlockMulti<BlockMinaPlanks>(MinaBlocks.PLANKS, BlockMinaPlanks.EnumType.subVariantNames).setRegistryName(MinaBlocks.PLANKS.getRegistryName()), "mina_planks", BlockMinaPlanks.EnumType.variantNamesPlanks);
		proxy.registerItem(new ItemBlockMulti<BlockMinaSapling>(MinaBlocks.SAPLING, BlockMinaPlanks.EnumType.subVariantNames).setRegistryName(MinaBlocks.SAPLING.getRegistryName()), "mina_sapling", BlockMinaPlanks.EnumType.variantNamesSaplings);
		proxy.registerItem(new ItemCloth(MinaBlocks.MILKY_GLASS).setRegistryName(MinaBlocks.MILKY_GLASS.getRegistryName()), "milky_glass", BlockCustomStainedGlass.milkyGlassVariantNames);
		proxy.registerItem(new ItemCloth(MinaBlocks.LIT_MILKY_GLASS).setRegistryName(MinaBlocks.LIT_MILKY_GLASS.getRegistryName()), "lit_milky_glass", BlockCustomStainedGlass.litMilkyGlassVariantNames);
		proxy.registerItem(new ItemBlockGiftBox(MinaBlocks.GIFT_BOX).setRegistryName(MinaBlocks.GIFT_BOX.getRegistryName()), "gift_box", BlockGiftBox.getVariantNames("gift_box"));
		proxy.registerItem(new ItemBlockCombined(MinaBlocks.COBBLEBRICKS).setRegistryName(MinaBlocks.COBBLEBRICKS.getRegistryName()), "cobblebricks", "cobblebrick_stone", "cobblebrick_slab");
		proxy.registerItem(new ItemBlockMulti<BlockElevatorFloor>(MinaBlocks.ELEVATOR_FLOOR, "default", "jumper").setRegistryName(MinaBlocks.ELEVATOR_FLOOR.getRegistryName()), "elevator_floor", "elevator_floor", "elevator_jumper");
		proxy.registerItem(new ItemBlockMulti<BlockCampbench>(MinaBlocks.CAMPBENCH, "acacia", "birch", "dark_oak", "spruce").setRegistryName(MinaBlocks.CAMPBENCH.getRegistryName()), "campbench", "campbench_acacia", "campbench_birch", "campbench_dark_oak", "campbench_spruce");
		proxy.registerItem(new ItemBlockLeaves(MinaBlocks.MINA_LEAVES_A, MinaBlocks.MINA_LEAVES_A.getUnlocalizedNames()).setRegistryName(MinaBlocks.MINA_LEAVES_A.getRegistryName()), "mina_leaves_a", MinaBlocks.MINA_LEAVES_A.getVariantNames());
		//proxy.registerItem(new ItemBlockMulti(MinaBlocks.MINA_LEAVES_B, MinaBlocks.MINA_LEAVES_B.getUnlocalizedNames()).setRegistryName(MinaBlocks.MINA_LEAVES_B.getRegistryName()), "mina_leaves_b", MinaBlocks.MINA_LEAVES_B.getVariantNames());
		proxy.registerItem(new ItemBlockCombined(MinaBlocks.PALM_LEAVES).setRegistryName(MinaBlocks.PALM_LEAVES.getRegistryName()), "palm_leaves", "palm_leaf_stack", "palm_leaf");
		proxy.registerItem(new ItemSlab(MinaBlocks.WOODEN_SLAB, MinaBlocks.WOODEN_SLAB, MinaBlocks.DOUBLE_WOODEN_SLAB).setRegistryName(MinaBlocks.WOODEN_SLAB.getRegistryName()), "mina_wooden_slab", BlockMinaPlanks.EnumType.variantNamesSlabs);
		
		itemBInit = true;
	}
}
