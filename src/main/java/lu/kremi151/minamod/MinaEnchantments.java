package lu.kremi151.minamod;

import lu.kremi151.minamod.enchantment.CustomEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class MinaEnchantments {

	private static EntityEquipmentSlot[] mh_only = new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND };

	public static final Enchantment EMERGENCY;
	public static final Enchantment BEHEADER;
	public static final Enchantment WOOD_BACKUP;
	public static final Enchantment FUSION;
	public static final Enchantment SPAWN_KEEPER;
	public static final Enchantment FREEZE_PROTECTION;

	static {
		EMERGENCY = new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, mh_only)
				.setName("emergency").setRegistryName(new ResourceLocation(MinaMod.MODID, "emergency"));
		BEHEADER = new CustomEnchantment(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, mh_only)
				.setName("beheader").setRegistryName(new ResourceLocation(MinaMod.MODID, "beheader"));
		WOOD_BACKUP = new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, mh_only)
				.setName("wood_backup").setRegistryName(new ResourceLocation(MinaMod.MODID, "wood_backup"));
		FUSION = new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, mh_only)
				.setName("fusion").setRegistryName(new ResourceLocation(MinaMod.MODID, "fusion"));
		SPAWN_KEEPER = new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.DIGGER, mh_only)
				.setName("spawn_keeper").setRegistryName(new ResourceLocation(MinaMod.MODID, "spawn_keeper"));
		FREEZE_PROTECTION = new CustomEnchantment(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR,
				new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
						EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET }).setLevelRange(1, 2)
								.setName("freeze_protection")
								.setRegistryName(new ResourceLocation(MinaMod.MODID, "freeze_protection"));
	}

	private static boolean init = false;

	static void registerEnchantments() {
		if (init)
			return;

		int start_id = 180;

		register(start_id++, EMERGENCY);
		register(start_id++, BEHEADER);
		register(start_id++, WOOD_BACKUP);
		register(start_id++, FUSION);
		register(start_id++, SPAWN_KEEPER);
		register(start_id++, FREEZE_PROTECTION);

		init = true;
	}

	private static void register(int id, Enchantment enc) {
		Enchantment.REGISTRY.register(id, enc.getRegistryName(), enc);
	}
}
