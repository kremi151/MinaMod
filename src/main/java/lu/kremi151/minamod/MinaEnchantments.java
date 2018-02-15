package lu.kremi151.minamod;

import lu.kremi151.minamod.enchantment.CustomEnchantment;
import lu.kremi151.minamod.enchantment.EnchantmentBeheader;
import lu.kremi151.minamod.enchantment.EnchantmentHarvestRange;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MinaMod.MODID)
public class MinaEnchantments {

	private static final EntityEquipmentSlot[] MAIN_HAND_ONLY = new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND };

	@ObjectHolder("emergency")
	public static final Enchantment EMERGENCY = null;
	
	@ObjectHolder("beheader")
	public static final Enchantment BEHEADER = null;
	
	@ObjectHolder("wood_backup")
	public static final Enchantment WOOD_BACKUP = null;
	
	@ObjectHolder("fusion")
	public static final Enchantment FUSION = null;
	
	@ObjectHolder("spawn_keeper")
	public static final Enchantment SPAWN_KEEPER = null;
	
	@ObjectHolder("freeze_protection")
	public static final Enchantment FREEZE_PROTECTION = null;
	
	@ObjectHolder("harvest_range")
	public static final Enchantment HARVEST_RANGE = null;

	static void register(IForgeRegistry<Enchantment> registry) {
		registry.register(new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("emergency").setRegistryName(new ResourceLocation(MinaMod.MODID, "emergency")));
		registry.register(new EnchantmentBeheader(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("beheader").setRegistryName(new ResourceLocation(MinaMod.MODID, "beheader")));
		registry.register(new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("wood_backup").setRegistryName(new ResourceLocation(MinaMod.MODID, "wood_backup")));
		registry.register(new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY, true)
				.setName("fusion").setRegistryName(new ResourceLocation(MinaMod.MODID, "fusion")));
		registry.register(new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.DIGGER, MAIN_HAND_ONLY)
				.setName("spawn_keeper").setRegistryName(new ResourceLocation(MinaMod.MODID, "spawn_keeper")));
		registry.register(new CustomEnchantment(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
						EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET }).setLevelRange(1, 2)
								.setName("freeze_protection")
								.setRegistryName(new ResourceLocation(MinaMod.MODID, "freeze_protection")));
		registry.register(new EnchantmentHarvestRange(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setLevelRange(1, 2).setName("harvest_range").setRegistryName(new ResourceLocation(MinaMod.MODID, "harvest_range")));
	}
}
