package lu.kremi151.minamod;

import lu.kremi151.minamod.potion.CustomPotion;
import lu.kremi151.minamod.potion.PotionDoge;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class MinaPotions {

	public static final Potion FREEZE;
	public static final Potion SATURATION;
	public static final Potion DOGE;

	static{
		FREEZE = new CustomPotion(true, 13713439).setIconIndex(0, 0)
				.setPotionName("potion.minamod.freeze").setRegistryName(MinaMod.createResourceLocation("freeze"));
		SATURATION = new CustomPotion(false, 1837285)
				.setIconIndex(0, 0).setPotionName("potion.minamod.saturation").setRegistryName(MinaMod.createResourceLocation("saturation"));
		DOGE = new PotionDoge(false, MinaUtils.convertRGBToDecimal(255, 128, 0)).setPotionName("potion.minamod.doge").setRegistryName(MinaMod.createResourceLocation("doge"));
	}
	
	static void register(IForgeRegistry<Potion> registry){
		registry.register(FREEZE);
		registry.register(SATURATION);
		registry.register(DOGE);
	}
}
