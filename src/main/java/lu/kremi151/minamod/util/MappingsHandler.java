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

	public static void handleMappings(FMLMissingMappingsEvent event){
		for(FMLMissingMappingsEvent.MissingMapping mapping : event.get()){
			if(mapping.type == GameRegistry.Type.ITEM){
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
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "mina_log_g1"))){
					println("Remapping old MinaMod item log type to new one (peppel by default)");
					mapping.remap(Item.getItemFromBlock(MinaBlocks.LOG_PEPPEL));
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "mina_leaves_g1"))){
					println("Remapping old MinaMod item leaf type to new one (peppel by default)");
					mapping.remap(Item.getItemFromBlock(MinaBlocks.MINA_LEAVES_A));
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "doge_seeds"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "kevikus_seeds"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "tracius_seeds"))){
					println("Remapping old berry item type to combined item, old ones will result in doge berrys #CollateralDamage");
					mapping.remap(MinaItems.BERRY_SEEDS);
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_peppel"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_cotton"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_chestnut"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_cherry"))){
					mapping.remap(Item.getItemFromBlock(MinaBlocks.MINA_LEAVES_A));
				}
			}else if(mapping.type == GameRegistry.Type.BLOCK){
				if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "mina_log_g1"))){
					println("Remapping old MinaMod block log type to new one (peppel by default)");
					mapping.remap(MinaBlocks.LOG_PEPPEL);
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "mina_leaves_g1"))){
					println("Remapping old MinaMod block leaf type to new one (peppel by default)");
					mapping.remap(MinaBlocks.MINA_LEAVES_A);
				}else if(mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_peppel"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_cotton"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_chestnut"))
						|| mapping.resourceLocation.equals(new ResourceLocation(MODID, "leaves_cherry"))){
					mapping.remap(MinaBlocks.MINA_LEAVES_A);
				}
			}
		}
	}
}
