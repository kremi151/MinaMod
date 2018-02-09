package lu.kremi151.minamod.util;

import static lu.kremi151.minamod.MinaMod.MODID;
import static lu.kremi151.minamod.MinaMod.println;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MODID)
public class MappingsHandler {
	
	private static final ResourceLocation OLD_LOG = new ResourceLocation(MODID, "mina_log_g1");
	private static final ResourceLocation DOGE_SEEDS = new ResourceLocation(MODID, "doge_seeds");
	private static final ResourceLocation KEVIKUS_SEEDS = new ResourceLocation(MODID, "kevikus_seeds");
	private static final ResourceLocation TRACIUS_SEEDS = new ResourceLocation(MODID, "tracius_seeds");
	private static final ResourceLocation OLD_AMULET = new ResourceLocation(MODID, "amulet");
	private static final ResourceLocation OLD_BAMBUS_ID = new ResourceLocation(MODID, "bambus_item");

	@SubscribeEvent
	protected static void handleItemMappings(RegistryEvent.MissingMappings<Item> event){
		for(Mapping<Item> mapping : event.getMappings()) {
			handleItemMapping(mapping);
		}
	}
	
	private static void handleItemMapping(Mapping<Item> mapping) {
		if(mapping.key.equals(OLD_LOG)){
			println("Remapping old MinaMod item log type to new one (peppel by default)");
			mapping.remap(Item.getItemFromBlock(MinaBlocks.LOG_PEPPEL));
		}else if(mapping.key.equals(DOGE_SEEDS)
				|| mapping.key.equals(KEVIKUS_SEEDS)
				|| mapping.key.equals(TRACIUS_SEEDS)){
			println("Remapping old berry item type to combined item, old ones will result in doge berrys #CollateralDamage");
			mapping.remap(MinaItems.BERRY_SEEDS);
		}else if(mapping.key.equals(OLD_AMULET)) {
			println("Remapping old amulet type to ender amulet");
			mapping.remap(MinaItems.AMULET_OF_ENDER);
		}else if(mapping.key.equals(OLD_BAMBUS_ID)) {
			mapping.remap(MinaItems.BAMBUS);
		}
	}
	
	@SubscribeEvent
	protected static void handleBlockMappings(RegistryEvent.MissingMappings<Block> event){
		for(Mapping<Block> mapping : event.getMappings()) {
			handleBlockMapping(mapping);
		}
	}
	
	private static void handleBlockMapping(Mapping<Block> mapping){
		if(mapping.key.equals(OLD_LOG)){
			println("Remapping old MinaMod block log type to new one (peppel by default)");
			mapping.remap(MinaBlocks.LOG_PEPPEL);
		}
	}
}
