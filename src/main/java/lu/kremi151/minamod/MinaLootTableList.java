package lu.kremi151.minamod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class MinaLootTableList {

	public static final ResourceLocation FLOWERSTORE;
	public static final ResourceLocation WOOKIE_HOUSE;
	public static final ResourceLocation SIEVE_SAND;
	public static final ResourceLocation SIEVE_GRAVEL;
	public static final ResourceLocation SIEVE_SOUL_SAND;

	static{
		FLOWERSTORE = LootTableList.register(new ResourceLocation(MinaMod.MODID, "chests/flowerstore"));
		WOOKIE_HOUSE = LootTableList.register(new ResourceLocation(MinaMod.MODID, "chests/wookie_house"));
		SIEVE_SAND = LootTableList.register(new ResourceLocation(MinaMod.MODID, "sieve/sand"));
		SIEVE_GRAVEL = LootTableList.register(new ResourceLocation(MinaMod.MODID, "sieve/gravel"));
		SIEVE_SOUL_SAND = LootTableList.register(new ResourceLocation(MinaMod.MODID, "sieve/soul_sand"));
	}
}
