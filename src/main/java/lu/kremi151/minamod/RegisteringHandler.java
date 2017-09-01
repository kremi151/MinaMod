package lu.kremi151.minamod;

import java.util.Iterator;
import java.util.LinkedList;

import lu.kremi151.minamod.proxy.CommonProxy;
import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.ItemRegistrationHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = MinaMod.MODID)
public final class RegisteringHandler {
	
	private static final LinkedList<Item> ITEM_BLOCKS = new LinkedList<>();
	
	private RegisteringHandler() {}

	@SubscribeEvent
	protected static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		MinaBlocks.registerBlocks((obj, name) -> {
			return new BlockRegistrationHandler(name, obj) {
				@Override
				public void submit() {
			    	if(obj.getRegistryName() == null){
			    		obj.setRegistryName(new ResourceLocation(MinaMod.MODID, name));
			    	}
					event.getRegistry().register(obj);
			    	if(!this.blockOnly) {
			    		ITEM_BLOCKS.add(this.itemBlockFactory.apply(obj));
			    	}
					if(this.oreName != null) {
				    	OreDictionary.registerOre(this.oreName, obj);
					}
					// TODO Auto-generated method stub
				}
			};
		});
	}
	
	@SubscribeEvent
	protected static void onRegisterItems(RegistryEvent.Register<Item> event) {
		Iterator<Item> it = ITEM_BLOCKS.iterator();
		while(it.hasNext()) {
			event.getRegistry().register(it.next());
			it.remove();
		}
		MinaItems.registerItems((obj, name) -> {
			return new ItemRegistrationHandler(name, obj) {
				@Override
				public void submit() {
			    	if(this.vanillaRegistration) {
			    		event.getRegistry().register(obj);
			    	}else {
			    		if(obj.getRegistryName() == null){
				    		obj.setRegistryName(new ResourceLocation(MinaMod.MODID, name));
				    	}
						event.getRegistry().register(obj);
						if(this.oreName != null) {
					    	OreDictionary.registerOre(this.oreName, obj);
						}
						// TODO Auto-generated method stub
			    	}
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
