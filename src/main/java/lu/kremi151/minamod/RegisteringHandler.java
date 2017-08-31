package lu.kremi151.minamod;

import lu.kremi151.minamod.proxy.CommonProxy;
import lu.kremi151.minamod.util.registration.CommonRegistrationHandler;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
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
	protected static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		MinaBlocks.registerBlocks((obj, name) -> {
			return new CommonRegistrationHandler<Block>(name, obj) {
				@Override
				public void submit() {
					// TODO Auto-generated method stub
				}
			};
		});
	}
	
	@SubscribeEvent
	protected static void onRegisterItems(RegistryEvent.Register<Item> event) {
		MinaItems.registerItems((obj, name) -> {
			return new CommonRegistrationHandler<Item>(name, obj) {
				@Override
				public void submit() {
					// TODO Auto-generated method stub
				}
			};
		});
	}
	
	@SubscribeEvent
	protected static void onRegisterModels(ModelRegistryEvent event) {
		CommonProxy proxy = MinaMod.getProxy();
		proxy.registerStateMappings();
		proxy.registerVariantNames();
		proxy.registerCustomMeshDefinitions();
		proxy.registerFluidModels();
	}
	
}
