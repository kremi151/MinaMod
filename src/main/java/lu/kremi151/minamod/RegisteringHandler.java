package lu.kremi151.minamod;

import lu.kremi151.minamod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MinaMod.MODID)
public final class RegisteringHandler {
	
	private RegisteringHandler() {}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		MinaBlocks.registerBlocks();
	}
	
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		MinaItems.registerItems();
	}
	
	@SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent event) {
		CommonProxy proxy = MinaMod.getProxy();
		proxy.registerStateMappings();
		proxy.registerVariantNames();
		proxy.registerFluidModels();
	}
	
}
