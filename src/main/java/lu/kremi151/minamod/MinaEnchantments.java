package lu.kremi151.minamod;

import lu.kremi151.minamod.enchantment.CustomEnchantment;
import lu.kremi151.minamod.enchantment.EnchantmentBeheader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		BEHEADER = new EnchantmentBeheader(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, mh_only)
				.setName("beheader").setRegistryName(new ResourceLocation(MinaMod.MODID, "beheader"));
		WOOD_BACKUP = new CustomEnchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, mh_only)
				.setName("wood_backup").setRegistryName(new ResourceLocation(MinaMod.MODID, "wood_backup"));
		FUSION = new CustomEnchantment(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, mh_only, true)
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
		if (init) return;

		GameRegistry.register(EMERGENCY);
		GameRegistry.register(BEHEADER);
		GameRegistry.register(WOOD_BACKUP);
		GameRegistry.register(FUSION);
		GameRegistry.register(SPAWN_KEEPER);
		GameRegistry.register(FREEZE_PROTECTION);

		init = true;
	}
}
