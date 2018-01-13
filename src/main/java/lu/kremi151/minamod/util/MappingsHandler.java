package lu.kremi151.minamod.util;

import static lu.kremi151.minamod.MinaMod.MODID;
import static lu.kremi151.minamod.MinaMod.println;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MappingsHandler {
	
	private static final ResourceLocation OLD_LOG = new ResourceLocation(MODID, "mina_log_g1");
	private static final ResourceLocation DOGE_SEEDS = new ResourceLocation(MODID, "doge_seeds");
	private static final ResourceLocation KEVIKUS_SEEDS = new ResourceLocation(MODID, "kevikus_seeds");
	private static final ResourceLocation TRACIUS_SEEDS = new ResourceLocation(MODID, "tracius_seeds");
	private static final ResourceLocation OLD_AMULET = new ResourceLocation(MODID, "amulet");
	private static final ResourceLocation OLD_BAMBUS_ID = new ResourceLocation(MODID, "bambus_item");
	
	private static void handleItemMapping(FMLMissingMappingsEvent.MissingMapping mapping){
		if(mapping.resourceLocation.equals(MinaBlocks.NAMIE_FLOWER.getRegistryName())
				|| mapping.resourceLocation.equals(MinaBlocks.STRAWBERRY_CROP.getRegistryName())
				|| mapping.resourceLocation.equals(MinaBlocks.BAMBUS_CROP.getRegistryName())
				|| mapping.resourceLocation.equals(MinaBlocks.RHUBARB_PLANT.getRegistryName())
				|| mapping.resourceLocation.equals(MinaBlocks.EFFECT_BUSH.getRegistryName())){
			println("Removing item mapping from block " + mapping.resourceLocation);
			try{//Hacky trick to bypass stupid check conditions
				ReflectionLoader.MissingMapping_setAction(mapping, FMLMissingMappingsEvent.Action.BLOCKONLY);
			}catch(Exception t){
				t.printStackTrace();
			}
		}else if(mapping.resourceLocation.equals(OLD_LOG)){
			println("Remapping old MinaMod item log type to new one (peppel by default)");
			mapping.remap(Item.getItemFromBlock(MinaBlocks.LOG_PEPPEL));
		}else if(mapping.resourceLocation.equals(DOGE_SEEDS)
				|| mapping.resourceLocation.equals(KEVIKUS_SEEDS)
				|| mapping.resourceLocation.equals(TRACIUS_SEEDS)){
			println("Remapping old berry item type to combined item, old ones will result in doge berrys #CollateralDamage");
			mapping.remap(MinaItems.BERRY_SEEDS);
		}else if(mapping.resourceLocation.equals(OLD_AMULET)) {
			println("Remapping old amulet type to ender amulet");
			mapping.remap(MinaItems.AMULET_OF_ENDER);
		}else if(mapping.resourceLocation.equals(OLD_BAMBUS_ID)) {
			mapping.remap(MinaItems.BAMBUS);
		}
	}
	
	private static void handleBlockMapping(FMLMissingMappingsEvent.MissingMapping mapping){
		if(mapping.resourceLocation.equals(OLD_LOG)){
			println("Remapping old MinaMod block log type to new one (peppel by default)");
			mapping.remap(MinaBlocks.LOG_PEPPEL);
		}
	}

	public static void handleMappings(FMLMissingMappingsEvent event){
		for(FMLMissingMappingsEvent.MissingMapping mapping : event.get()){
			if(mapping.type == GameRegistry.Type.ITEM){
				handleItemMapping(mapping);
			}else if(mapping.type == GameRegistry.Type.BLOCK){
				handleBlockMapping(mapping);
			}
		}
	}
}
