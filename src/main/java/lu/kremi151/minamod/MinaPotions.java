package lu.kremi151.minamod;

import lu.kremi151.minamod.potion.CustomPotion;
import lu.kremi151.minamod.potion.PotionDoge;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MinaMod.MODID)
public class MinaPotions {
	
	@ObjectHolder("freeze")
	public static final Potion FREEZE = null;

	@ObjectHolder("saturation")
	public static final Potion SATURATION = null;

	@ObjectHolder("doge")
	public static final Potion DOGE = null;
	
	static void register(IForgeRegistry<Potion> registry){
		registry.register(new CustomPotion(true, 13713439).setIconIndex(0, 0)
				.setPotionName("potion.minamod.freeze").setRegistryName(MinaMod.createResourceLocation("freeze")));
		registry.register(new CustomPotion(false, 1837285).setIconIndex(0, 0)
				.setPotionName("potion.minamod.saturation").setRegistryName(MinaMod.createResourceLocation("saturation")));
		registry.register(new PotionDoge(false, MinaUtils.convertRGBToDecimal(255, 128, 0))
				.setPotionName("potion.minamod.doge").setRegistryName(MinaMod.createResourceLocation("doge")));
	}
}
