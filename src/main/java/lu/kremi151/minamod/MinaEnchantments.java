package lu.kremi151.minamod;

import lu.kremi151.minamod.enchantment.CustomEnchantment;
import lu.kremi151.minamod.enchantment.EnchantmentBeheader;
import lu.kremi151.minamod.enchantment.EnchantmentHarvestRange;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class MinaEnchantments {

	private static final EntityEquipmentSlot[] MAIN_HAND_ONLY = new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND };

	public static final Enchantment EMERGENCY;
	public static final Enchantment BEHEADER;
	public static final Enchantment WOOD_BACKUP;
	public static final Enchantment FUSION;
	public static final Enchantment SPAWN_KEEPER;
	public static final Enchantment FREEZE_PROTECTION;
	public static final Enchantment HARVEST_RANGE;

	static {
		EMERGENCY = new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("emergency").setRegistryName(new ResourceLocation(MinaMod.MODID, "emergency"));
		BEHEADER = new EnchantmentBeheader(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("beheader").setRegistryName(new ResourceLocation(MinaMod.MODID, "beheader"));
		WOOD_BACKUP = new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setName("wood_backup").setRegistryName(new ResourceLocation(MinaMod.MODID, "wood_backup"));
		FUSION = new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY, true)
				.setName("fusion").setRegistryName(new ResourceLocation(MinaMod.MODID, "fusion"));
		SPAWN_KEEPER = new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.DIGGER, MAIN_HAND_ONLY)
				.setName("spawn_keeper").setRegistryName(new ResourceLocation(MinaMod.MODID, "spawn_keeper"));
		FREEZE_PROTECTION = new CustomEnchantment(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
						EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET }).setLevelRange(1, 2)
								.setName("freeze_protection")
								.setRegistryName(new ResourceLocation(MinaMod.MODID, "freeze_protection"));
		HARVEST_RANGE = new EnchantmentHarvestRange(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, MAIN_HAND_ONLY)
				.setLevelRange(1, 2)
				.setName("harvest_range")
				.setRegistryName(new ResourceLocation(MinaMod.MODID, "harvest_range"));
	}

	static void register(IForgeRegistry<Enchantment> registry) {
		registry.register(EMERGENCY);
		registry.register(BEHEADER);
		registry.register(WOOD_BACKUP);
		registry.register(FUSION);
		registry.register(SPAWN_KEEPER);
		registry.register(FREEZE_PROTECTION);
		registry.register(HARVEST_RANGE);
	}
}
