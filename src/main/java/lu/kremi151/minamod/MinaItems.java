package lu.kremi151.minamod;

import java.lang.reflect.Field;

import lu.kremi151.minamod.block.BlockCustomStainedGlass;
import lu.kremi151.minamod.block.BlockGiftBox;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockStool;
import lu.kremi151.minamod.block.BlockTable;
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
import lu.kremi151.minamod.item.ItemCombiner;
import lu.kremi151.minamod.item.ItemCopperIngot;
import lu.kremi151.minamod.item.ItemCustomArmor;
import lu.kremi151.minamod.item.ItemCustomAxe;
import lu.kremi151.minamod.item.ItemCustomHoe;
import lu.kremi151.minamod.item.ItemCustomPickaxe;
import lu.kremi151.minamod.item.ItemCustomShovel;
import lu.kremi151.minamod.item.ItemCustomSword;
import lu.kremi151.minamod.item.ItemDrill;
import lu.kremi151.minamod.item.ItemEmptyPot;
import lu.kremi151.minamod.item.ItemEnergyDiagnostic;
import lu.kremi151.minamod.item.ItemFoodContainer;
import lu.kremi151.minamod.item.ItemGoldenCoin;
import lu.kremi151.minamod.item.ItemHerb;
import lu.kremi151.minamod.item.ItemHerbGuide;
import lu.kremi151.minamod.item.ItemHerbMixture;
import lu.kremi151.minamod.item.ItemHoneyPot;
import lu.kremi151.minamod.item.ItemKatana;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemKeyChain;
import lu.kremi151.minamod.item.ItemMilkBottle;
import lu.kremi151.minamod.item.ItemPowder;
import lu.kremi151.minamod.item.ItemRareEarth;
import lu.kremi151.minamod.item.ItemRubberTreeBranch;
import lu.kremi151.minamod.item.ItemSickle;
import lu.kremi151.minamod.item.ItemSketch;
import lu.kremi151.minamod.item.ItemSoulPearl;
import lu.kremi151.minamod.item.ItemStrawberry;
import lu.kremi151.minamod.item.ItemUnpackedGift;
import lu.kremi151.minamod.item.block.ItemBlockCable;
import lu.kremi151.minamod.item.block.ItemBlockCombined;
import lu.kremi151.minamod.item.block.ItemBlockGiftBox;
import lu.kremi151.minamod.item.block.ItemBlockLeaves;
import lu.kremi151.minamod.item.block.ItemBlockMulti;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import lu.kremi151.minamod.util.registration.ItemRegistrationHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;

@ObjectHolder(MinaMod.MODID)
public class MinaItems {

	public static final Item EVER_SNOW = null;
	public static final Item NAMIE_SEEDS = null;
	public static final Item NAMIE_FRUIT = null;
	public static final Item MILK_BOTTLE = null;
	public static final Item CITRIN = null;
	public static final Item CITRIN_SWORD = null;
	public static final Item CITRIN_AXE = null;
	public static final Item CITRIN_PICKAXE = null;
	public static final Item CITRIN_SHOVEL = null;
	public static final Item CITRIN_HOE = null;
	public static final Item HONEYWABE = null;
	public static final Item STRAWBERRY = null;
	public static final Item BAMBUS = null;
	public static final Item BAMBUS_SWORD = null;
	public static final Item BAMBUS_AXE = null;
	public static final Item BAMBUS_PICKAXE = null;
	public static final Item BAMBUS_SHOVEL = null;
	public static final Item BAMBUS_HOE = null;
	@ObjectHolder("rhubarb_item") public static final Item RHUBARB = null;
	public static final Item CHESTNUT = null;
	public static final Item CHESTNUT_COOKED = null;
	public static final Item CHERRY = null;
	public static final Item OBSIDIAN_FRAGMENT = null;
	public static final Item OBSIDIAN_SWORD = null;
	public static final Item OBSIDIAN_AXE = null;
	public static final Item OBSIDIAN_PICKAXE = null;
	public static final Item OBSIDIAN_SHOVEL = null;
	public static final Item OBSIDIAN_HOE = null;
	public static final Item CITRIN_HELMET = null;
	public static final Item CITRIN_CHESTPLATE = null;
	public static final Item CITRIN_LEGGINGS = null;
	public static final Item CITRIN_BOOTS = null;
	public static final Item KEY = null;
	public static final Item CHIP = null;
	public static final Item OBSIDIAN_HELMET = null;
	public static final Item OBSIDIAN_CHESTPLATE = null;
	public static final Item OBSIDIAN_LEGGINGS = null;
	public static final Item OBSIDIAN_BOOTS = null;
	public static final Item BLACK_PEARL = null;
	public static final Item WHITE_PEARL = null;
	public static final Item HARMONY_PEARL = null;
	public static final Item POULET_SPECIAL_PIQUANT = null;
	public static final Item CHILI = null;
	public static final Item CHILI_POWDER = null;
	public static final Item SWEET_SOUR = null;
	public static final Item GOLDEN_COIN = null;
	public static final Item PLATINUM_INGOT = null;
	public static final Item SOUL_PEARL = null;
	public static final Item PLATINUM_HELMET = null;
	public static final Item PLATINUM_CHESTPLATE = null;
	public static final Item PLATINUM_LEGGINGS = null;
	public static final Item PLATINUM_BOOTS = null;
	public static final Item HERB = null;
	public static final Item POWDER = null;
	public static final Item EMPTY_POT = null;
	public static final Item HONEY_POT = null;
	public static final Item SAPPHIRE = null;
	public static final Item RUBY = null;
	public static final Item TURTLE_SHELL = null;
	@ObjectHolder("turtle_shell_helmet") public static final Item TURTLE_HELMET = null;
	@ObjectHolder("turtle_shell_chestplate") public static final Item TURTLE_CHESTPLATE = null;
	@ObjectHolder("turtle_shell_leggings") public static final Item TURTLE_LEGGINGS = null;
	@ObjectHolder("turtle_shell_boots") public static final Item TURTLE_BOOTS = null;
	public static final Item MIXTURE = null;
	public static final Item WOOD_SICKLE = null;
	public static final Item STONE_SICKLE = null;
	public static final Item IRON_SICKLE = null;
	@ObjectHolder("colored_written_book") public static final Item COLORED_BOOK = null;
	public static final Item COTTON = null;
	public static final Item BERRY = null;
	public static final Item BERRY_SEEDS = null;
	public static final Item PUMPKIN_SOUP = null;
	public static final Item CANDY_CANE = null;
	public static final Item ICE_SWORD = null;
	public static final Item GOULASH = null;
	public static final Item KATANA = null;
	public static final Item COIN_BAG = null;
	public static final Item AMULET_OF_ENDER = null;
	public static final Item AMULET_OF_EXPERIENCE = null;
	public static final Item AMULET_OF_RETURN = null;
	public static final Item AMULET_OF_REGENERATION = null;
	public static final Item AMULET_OF_MERMAID = null;
	public static final Item AMULET_OF_HARMONY = null;
	public static final Item BATTERY = null;
	public static final Item HERB_GUIDE = null;
	public static final Item CHICKEN_NUGGETS = null;
	public static final Item FLOUR = null;
	public static final Item UNPACKED_GIFT = null;
	public static final Item KEY_CHAIN = null;
	public static final Item SKETCH = null;
	public static final Item COMBINER = null;
	@ObjectHolder("rare_earth_ore") public static final Item RARE_EARTH = null;
	public static final Item DRILL = null;
	public static final Item COPPER_INGOT = null;
	public static final Item COPPER_NUGGET = null;
	public static final Item RUBBER_TREE_BRANCH = null;
	public static final Item RUBBER = null;
	public static final Item DIAGNOSE_TOOL = null;
	@ObjectHolder("totem_of_legacy") public static final Item TOTEM_LEGACY = null;

	public static void registerItems(IRegistrationInterface<Item, ItemRegistrationHandler> registry) {
		registerItemBlocks(registry);

		registry.register(new Item().setCreativeTab(CreativeTabs.MISC), "ever_snow").autoname().submit();
		registry.register(new ItemSeeds(MinaBlocks.NAMIE_FLOWER, Blocks.FARMLAND), "namie_seeds").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.FOOD), "namie_fruit").autoname().submit();
		registry.register(new ItemMilkBottle(), "milk_bottle").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "citrin").autoname().ore("gemCitrin").submit();
		registry.register(new ItemCustomSword(MinaToolMaterials.CITRIN), "citrin_sword").autoname().submit();
		registry.register(new ItemCustomAxe(MinaToolMaterials.CITRIN), "citrin_axe").autoname().submit();
		registry.register(new ItemCustomPickaxe(MinaToolMaterials.CITRIN), "citrin_pickaxe").autoname().submit();
		registry.register(new ItemCustomShovel(MinaToolMaterials.CITRIN), "citrin_shovel").autoname().submit();
		registry.register(new ItemCustomHoe(MinaToolMaterials.CITRIN), "citrin_hoe").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "honeywabe").autoname().submit();
		registry.register(new ItemStrawberry(), "strawberry").autoname().variantNames("strawberry", "ruby_strawberry", "sapphire_strawberry", "citrin_strawberry").ore().submit();
		registry.register(new ItemBambus(), "bambus").autoname().submit();
		registry.register(new ItemCustomSword(MinaToolMaterials.BAMBUS), "bambus_sword").autoname().submit();
		registry.register(new ItemCustomAxe(MinaToolMaterials.BAMBUS), "bambus_axe").autoname().submit();
		registry.register(new ItemCustomPickaxe(MinaToolMaterials.BAMBUS), "bambus_pickaxe").autoname().submit();
		registry.register(new ItemCustomShovel(MinaToolMaterials.BAMBUS), "bambus_shovel").autoname().submit();
		registry.register(new ItemCustomHoe(MinaToolMaterials.BAMBUS), "bambus_hoe").autoname().submit();
		registry.register(new Item().setUnlocalizedName("rhubarb").setCreativeTab(CreativeTabs.MATERIALS), "rhubarb_item").submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.FOOD), "chestnut").autoname().ore().submit();
		registry.register(new ItemFood(4, 0f, false).setCreativeTab(CreativeTabs.FOOD), "chestnut_cooked").autoname().submit();
		registry.register(new ItemFood(1, 0.2f, false), "cherry").autoname().ore().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "obsidian_fragment").autoname().submit();
		registry.register(new ItemCustomSword(MinaToolMaterials.OBSIDIAN), "obsidian_sword").autoname().submit();
		registry.register(new ItemCustomAxe(MinaToolMaterials.OBSIDIAN), "obsidian_axe").autoname().submit();
		registry.register(new ItemCustomPickaxe(MinaToolMaterials.OBSIDIAN), "obsidian_pickaxe").autoname().submit();
		registry.register(new ItemCustomShovel(MinaToolMaterials.OBSIDIAN), "obsidian_shovel").autoname().submit();
		registry.register(new ItemCustomHoe(MinaToolMaterials.OBSIDIAN), "obsidian_hoe").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.HEAD), "citrin_helmet").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.CHEST), "citrin_chestplate").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.LEGS), "citrin_leggings").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.CITRIN, 0, EntityEquipmentSlot.FEET), "citrin_boots").autoname().submit();
		registry.register(new ItemKey(), "key").autoname().submit();
		registry.register(new ItemChip(), "chip").autoname().variantNames(ItemChip.getVariantNames()).submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.HEAD), "obsidian_helmet").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.CHEST), "obsidian_chestplate").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.LEGS), "obsidian_leggings").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.OBSIDIAN, 0, EntityEquipmentSlot.FEET), "obsidian_boots").autoname().submit();
		if(FeatureList.enable_ice_altar){
			registry.register(new Item().setCreativeTab(CreativeTabs.MISC), "black_pearl").autoname().submit();
			registry.register(new Item().setCreativeTab(CreativeTabs.MISC), "white_pearl").autoname().submit();
			registry.register(new Item().setCreativeTab(CreativeTabs.MISC), "harmony_pearl").autoname().submit();
			registry.register(new ItemAmulet(), "amulet_of_harmony").autoname().submit();
		}
		registry.register(new ItemFoodContainer(14, 0.8F, false, Items.BOWL).setCreativeTab(CreativeTabs.FOOD), "poulet_special_piquant").autoname().submit();
		registry.register(new ItemChili(), "chili").autoname().ore().submit();
		registry.register(new ItemChiliPowder(), "chili_powder").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "sweet_sour").autoname().submit();
		registry.register(new ItemGoldenCoin(), "golden_coin").autoname().variantNames("golden_coin", "five_golden_coins").submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "platinum_ingot").autoname().ore("ingotPlatinum").submit();
		registry.register(new ItemSoulPearl(), "soul_pearl").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.HEAD), "platinum_helmet").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.CHEST), "platinum_chestplate").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.LEGS), "platinum_leggings").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.PLATINUM, 0, EntityEquipmentSlot.FEET), "platinum_boots").autoname().submit();
		registry.register(new ItemHerb(), "herb").autoname().submit();
		registry.register(new ItemPowder(), "powder").autoname().submit();
		registry.register(new ItemEmptyPot(), "empty_pot").autoname().submit();
		registry.register(new ItemHoneyPot(), "honey_pot").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "ruby").autoname().ore("gemRuby").submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "sapphire").autoname().ore("gemSapphire").submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "turtle_shell").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.HEAD), "turtle_shell_helmet").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.CHEST), "turtle_shell_chestplate").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.LEGS), "turtle_shell_leggings").autoname().submit();
		registry.register(new ItemCustomArmor(MinaArmorMaterial.TURTLE_SHELL, 0, EntityEquipmentSlot.FEET), "turtle_shell_boots").autoname().submit();
		registry.register(new ItemHerbMixture(), "mixture").autoname().submit();
		registry.register(new ItemSickle(ToolMaterial.WOOD), "wood_sickle").autoname().submit();
		registry.register(new ItemSickle(ToolMaterial.STONE), "stone_sickle").autoname().submit();
		registry.register(new ItemSickle(ToolMaterial.IRON), "iron_sickle").autoname().submit();
		registry.register(new ItemColoredWrittenBook().setUnlocalizedName("writtenBook"), "colored_written_book").submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "cotton").autoname().ore().submit();
		registry.register(new ItemBerry(5, 1f, false), "berry").autoname().variantNames(ItemBerry.VARIANT_NAMES).submit();
		registry.register(new ItemBerrySeed(), "berry_seeds").autoname().variantNames("doge_seeds", "kevikus_seeds", "tracius_seeds").submit();
		registry.register(new ItemSoup(10), "pumpkin_soup").autoname().submit();
		registry.register(new ItemFood(2, 0.6f, false), "candy_cane").autoname().submit();
		registry.register(new ItemSword(MinaToolMaterials.ICE), "ice_sword").autoname().submit();
		registry.register(new ItemSoup(12), "goulash").autoname().submit();
		registry.register(new ItemKatana(), "katana").autoname().submit();
		registry.register(new ItemCoinBag(), "coin_bag").autoname().submit();
		registry.register(new ItemAmulet.Syncable(), "amulet_of_ender").autoname().submit();
		registry.register(new ItemAmulet.Syncable(), "amulet_of_experience").autoname().submit();
		registry.register(new ItemAmulet.Syncable(), "amulet_of_return").autoname().submit();
		registry.register(new ItemAmulet.Syncable(), "amulet_of_regeneration").autoname().submit();
		registry.register(new ItemAmulet.Syncable(), "amulet_of_mermaid").autoname().submit();
		registry.register(new ItemBattery(), "battery").autoname().submit();
		registry.register(new ItemHerbGuide().setRegistryName(MinaMod.MODID, "herb_guide"), "herb_guide").autoname().submitSimple();
		registry.register(new ItemFood(8, 0.6f, false), "chicken_nuggets").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "flour").autoname().ore().submit();
		registry.register(new ItemUnpackedGift(), "unpacked_gift").autoname().variantNames(BlockGiftBox.getVariantNames("unpacked_gift")).submit();
		registry.register(new ItemKeyChain(), "key_chain").autoname().submit();
		registry.register(new ItemSketch(), "sketch").autoname().submit();
		registry.register(new ItemCombiner(), "combiner").autoname().submit();
		registry.register(new ItemRareEarth().setUnlocalizedName("rare_earth"), "rare_earth_ore").variantNames(ItemRareEarth.VARIANT_NAMES).submit();
		registry.register(new ItemDrill().setRegistryName(MinaMod.MODID, "drill"), "drill").autoname().submitSimple();
		registry.register(new ItemCopperIngot(), "copper_ingot").autoname().submit();
		registry.register(new ItemCopperIngot(), "copper_nugget").autoname().submit();
		registry.register(new ItemRubberTreeBranch(), "rubber_tree_branch").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MATERIALS), "rubber").autoname().submit();
		registry.register(new ItemEnergyDiagnostic(), "diagnose_tool").autoname().submit();
		registry.register(new Item().setCreativeTab(CreativeTabs.MISC), "totem_of_legacy").autoname().submit();
		
		registerOreEntries();//Final step, when all items are registered
	}
	
	private static void registerItemBlocks(IRegistrationInterface<Item, ItemRegistrationHandler> registry){
		BlockTable.registerTableItems(registry);
		BlockStool.registerStoolItems(registry);
		registry.register(new ItemBlockMulti(MinaBlocks.PLANKS, BlockMinaPlanks.EnumType.subVariantNames).setRegistryName(MinaBlocks.PLANKS.getRegistryName()), "mina_planks").variantNames(BlockMinaPlanks.EnumType.variantNamesPlanks).submit();
		registry.register(new ItemBlockMulti(MinaBlocks.SAPLING, BlockMinaPlanks.EnumType.subVariantNames).setRegistryName(MinaBlocks.SAPLING.getRegistryName()), "mina_sapling").variantNames(BlockMinaPlanks.EnumType.variantNamesSaplings).submit();
		registry.register(new ItemCloth(MinaBlocks.MILKY_GLASS).setRegistryName(MinaBlocks.MILKY_GLASS.getRegistryName()), "milky_glass").variantNames(BlockCustomStainedGlass.milkyGlassVariantNames).submit();
		registry.register(new ItemCloth(MinaBlocks.LIT_MILKY_GLASS).setRegistryName(MinaBlocks.LIT_MILKY_GLASS.getRegistryName()), "lit_milky_glass").variantNames(BlockCustomStainedGlass.litMilkyGlassVariantNames).submit();
		registry.register(new ItemBlockGiftBox(MinaBlocks.GIFT_BOX).setRegistryName(MinaBlocks.GIFT_BOX.getRegistryName()), "gift_box").variantNames(BlockGiftBox.getVariantNames("gift_box")).submit();
		registry.register(new ItemBlockCombined(MinaBlocks.COBBLEBRICKS).setRegistryName(MinaBlocks.COBBLEBRICKS.getRegistryName()), "cobblebricks").variantNames("cobblebrick_stone", "cobblebrick_slab").submit();
		registry.register(new ItemBlockMulti(MinaBlocks.ELEVATOR_FLOOR, "default", "jumper").setRegistryName(MinaBlocks.ELEVATOR_FLOOR.getRegistryName()), "elevator_floor").variantNames("elevator_floor", "elevator_jumper").submit();
		registry.register(new ItemBlockMulti(MinaBlocks.CAMPBENCH, "acacia", "birch", "dark_oak", "spruce").setRegistryName(MinaBlocks.CAMPBENCH.getRegistryName()), "campbench").variantNames("campbench_acacia", "campbench_birch", "campbench_dark_oak", "campbench_spruce").submit();
		registry.register(new ItemBlockLeaves(MinaBlocks.MINA_LEAVES_A, MinaBlocks.MINA_LEAVES_A.getUnlocalizedNames()).setRegistryName(MinaBlocks.MINA_LEAVES_A.getRegistryName()), "mina_leaves_a").variantNames(MinaBlocks.MINA_LEAVES_A.getVariantNames()).submit();
		//registry.register(new ItemBlockMulti(MinaBlocks.MINA_LEAVES_B, MinaBlocks.MINA_LEAVES_B.getUnlocalizedNames()).setRegistryName(MinaBlocks.MINA_LEAVES_B.getRegistryName()), "mina_leaves_b", MinaBlocks.MINA_LEAVES_B.getVariantNames());
		registry.register(new ItemBlockCombined(MinaBlocks.PALM_LEAVES).setRegistryName(MinaBlocks.PALM_LEAVES.getRegistryName()), "palm_leaves").variantNames("palm_leaf_stack", "palm_leaf").submit();
		registry.register(new ItemSlab(MinaBlocks.WOODEN_SLAB, MinaBlocks.WOODEN_SLAB, MinaBlocks.DOUBLE_WOODEN_SLAB).setRegistryName(MinaBlocks.WOODEN_SLAB.getRegistryName()), "mina_wooden_slab").variantNames(BlockMinaPlanks.EnumType.variantNamesSlabs).submit();
		registry.register(new ItemBlockCable(MinaBlocks.CABLE).setRegistryName(MinaBlocks.CABLE.getRegistryName()), "cable").submit();
	}

	private static void registerOreEntries(){
		OreDictionary.registerOre("plankWood", new ItemStack(MinaBlocks.PLANKS, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	public static void verifyObjectHolders() throws IllegalArgumentException, IllegalAccessException {
		Field fields[] = MinaItems.class.getDeclaredFields();
		for(Field field : fields) {
			if(field.get(null) == null) {
				System.err.println("ObjectHolder MinaItems::" + field.getName() + " is null");
			}
		}
	}
}
