package lu.kremi151.minamod.util.registration.proxy;

import java.util.Iterator;
import java.util.LinkedList;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.item.ItemHerbGuide;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRegisteringProxy extends RegisteringProxy{

	private final LinkedList<VariantsData> variantsStack = new LinkedList<VariantsData>();

	@Override
	public void addItemVariants(Item item, String name, String... variantNames) {
		if(variantNames == null || variantNames.length == 0) {
			variantNames = new String[] {name};
		}
		variantsStack.add(new ItemVariantsData(item, name, MinaUtils.deserializeResourceLocations(MinaMod.MODID, variantNames)));
	}
	
	@Override
	public void addItemVariants(Block block, String name, String... variantNames) {
		if(variantNames == null || variantNames.length == 0) {
			variantNames = new String[] {name};
		}
		variantsStack.add(new BlockVariantsData(block, name, MinaUtils.deserializeResourceLocations(MinaMod.MODID, variantNames)));
	}
	
	@Override
	public void registerItemModels() {
		Iterator<VariantsData> it = variantsStack.iterator();
		while (it.hasNext()) {
			VariantsData bv = it.next();
			registerVariantNamesForItem(bv.getItem(), bv.name, bv.variantNames);
			it.remove();
		}
	}
	
	private void registerVariantNamesForItem(Item item, String name, ResourceLocation... variantNames) {
		if(item == Items.AIR){
			throw new RuntimeException("Cannot register mesh definition for air item");
		}
		try{
			boolean valid_vn = variantNames != null && variantNames.length > 0;
			if (valid_vn) {				
				if (variantNames.length == 1) {
					ModelLoader.setCustomMeshDefinition(item, new StaticMeshDefinition(new ModelResourceLocation(variantNames[0], "inventory")));
				} else {
					ModelBakery.registerItemVariants(item, variantNames);
					for (int i = 0; i < variantNames.length; i++) {
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(variantNames[i], "inventory"));
					}
				}
				
			} else {
				ModelLoader.setCustomMeshDefinition(item, new StaticMeshDefinition(new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, name), "inventory")));
			}
			
		}catch(RuntimeException e){
			MinaMod.errorln("Error registering variant names for item \"" + name + "\" [" + item.getClass() + "]");
			e.printStackTrace();
			throw(e);
		}
	}
	
	@Override
	public void registerCustomMeshDefinitions() {
		ModelBakery.registerItemVariants(MinaItems.HERB_GUIDE, MinaUtils.deserializeResourceLocations(MinaMod.MODID, "herb_guide", "herb_guide_completed"));
		ModelBakery.registerItemVariants(MinaItems.DRILL, MinaUtils.deserializeResourceLocations(MinaMod.MODID, "drill", "drill_broken"));
		ModelLoader.setCustomMeshDefinition(MinaItems.HERB_GUIDE, new HerbGuideMeshDefinition());
		ModelLoader.setCustomMeshDefinition(MinaItems.DRILL, new DrillMeshDefinition());
	}

	@SideOnly(Side.CLIENT)
	private abstract class VariantsData<T extends IForgeRegistryEntry<T>> {

		final T obj;
		final String name;
		final ResourceLocation[] variantNames;

		private VariantsData(T obj, String name, ResourceLocation... variantNames) {
			this.obj = obj;
			this.name = name;
			this.variantNames = variantNames;
		}
		
		protected abstract Item getItem();
	}

	@SideOnly(Side.CLIENT)
	private class ItemVariantsData extends VariantsData<Item> {
		
		private ItemVariantsData(Item item, String name, ResourceLocation... variantNames) {
			super(item, name, variantNames);
		}

		@Override
		protected Item getItem() {
			return obj;
		}
		
	}

	@SideOnly(Side.CLIENT)
	private class BlockVariantsData extends VariantsData<Block> {
		
		private BlockVariantsData(Block block, String name, ResourceLocation... variantNames) {
			super(block, name, variantNames);
		}

		@Override
		protected Item getItem() {
			return Item.getItemFromBlock(obj);
		}
		
	}

	@SideOnly(Side.CLIENT)
	private class StaticMeshDefinition implements ItemMeshDefinition {

		private ModelResourceLocation m;

		private StaticMeshDefinition(ModelResourceLocation m) {
			this.m = m;
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return m;
		}

	}
	
	@SideOnly(Side.CLIENT)
	private class HerbGuideMeshDefinition implements ItemMeshDefinition{

		private final ModelResourceLocation COMPLETED = new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, "herb_guide_completed"), "inventory");
		private final ModelResourceLocation INCOMPLETED = new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, "herb_guide"), "inventory");

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			if(ItemHerbGuide.percentageCompleted(stack) >= 1.0f) {
				return COMPLETED;
			}else {
				return INCOMPLETED;
			}
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	private class DrillMeshDefinition implements ItemMeshDefinition{

		private final ModelResourceLocation INTACT = new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, "drill"), "inventory");
		private final ModelResourceLocation DEFECT = new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, "drill_broken"), "inventory");

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			if(stack.getItemDamage() >= stack.getMaxDamage()) {
				return DEFECT;
			}else {
				return INTACT;
			}
		}
		
	}

}
