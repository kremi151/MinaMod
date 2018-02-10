package lu.kremi151.minamod;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang3.tuple.Pair;

import lu.kremi151.minamod.proxy.CommonProxy;
import lu.kremi151.minamod.util.VillagerHelper;
import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.ItemRegistrationHandler;
import lu.kremi151.minamod.util.registration.proxy.RegisteringProxy;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = MinaMod.MODID)
public final class RegisteringHandler {
	
	private static final LinkedList<Item> ITEM_BLOCKS = new LinkedList<>();
	private static final LinkedList<Pair<Block, String>> BLOCK_ORES = new LinkedList<>();
	
	@SidedProxy(modId = MinaMod.MODID, clientSide = "lu.kremi151.minamod.util.registration.proxy.ClientRegisteringProxy", serverSide = "lu.kremi151.minamod.util.registration.proxy.RegisteringProxy")
	private static RegisteringProxy proxy;
	
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
				    	BLOCK_ORES.add(Pair.of(this.obj, this.oreName));
					}
					if(!this.blockOnly) {
						proxy.addItemVariants(obj, name, this.variantNames);
					}
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
						proxy.addItemVariants(obj, name, this.variantNames);
			    	}
				}
			};
		});
		Iterator<Pair<Block, String>> it2 = BLOCK_ORES.iterator();
		while(it2.hasNext()) {
			Pair<Block, String> pair = it2.next();
			OreDictionary.registerOre(pair.getRight(), pair.getLeft());
			it2.remove();
		}
	}
	
	@SubscribeEvent
	protected static void onRegisterModels(ModelRegistryEvent event) {
		CommonProxy main_proxy = MinaMod.getProxy();
		main_proxy.registerStateMappings();
		proxy.registerItemModels();
		proxy.registerCustomMeshDefinitions();
		main_proxy.registerFluidModels();
	}
	
	@SubscribeEvent
	protected static void onModelBake(ModelBakeEvent event) {
		MinaMod.getProxy().handleModelBakeEvent(event);
	}
	
	@SubscribeEvent
	protected static void onPreStitch(TextureStitchEvent.Pre event) {
		MinaMod.getProxy().handleTexturePreStitch(event);
	}
	
	@SubscribeEvent
	protected static void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event) {
		VillagerHelper.instance().register(event.getRegistry());
	}
	
	@SubscribeEvent
	protected static void onRegisterEnchantments(RegistryEvent.Register<Enchantment> event) {
		MinaEnchantments.register(event.getRegistry());
	}
	
	@SubscribeEvent
	protected static void onRegisterPotions(RegistryEvent.Register<Potion> event) {
		MinaPotions.register(event.getRegistry());
	}
	
	@SubscribeEvent
	protected static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		MinaSounds.register(event.getRegistry());
	}
	
}
