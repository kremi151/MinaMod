package lu.kremi151.minamod.entity.fish;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class FishTypes {

	public static final FishType HIKARI = new FishType("hikari", new ResourceLocation(MinaMod.MODID,"textures/entity/fish/hikari.png"));
	public static final FishType HIUTSURI = new FishType("hiutsuri", new ResourceLocation(MinaMod.MODID,"textures/entity/fish/hi_utsuri.png"));
	public static final FishType SHIROUTSURI = new FishType("shiroutsuri", new ResourceLocation(MinaMod.MODID,"textures/entity/fish/shiro_utsuri.png"));
	
	public static void register(IForgeRegistry<FishType> registry){
		register(registry, HIKARI);
		register(registry, HIUTSURI);
		register(registry, SHIROUTSURI);
	}
	
	/**
	 * Do not call this from outside this mod
	 */
	private static void register(IForgeRegistry<FishType> registry, FishType type){
		type.setRegistryName(new ResourceLocation(MinaMod.MODID, type.getName()));
		registry.register(type);
	}
}
