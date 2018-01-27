package lu.kremi151.minamod.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.stats.snack.ISnack;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.interfaces.IMixtureIngredient;
import lu.kremi151.minamod.item.ItemHerbMixture;
import lu.kremi151.minamod.item.ItemHerbMixture.HerbMixtureSnack;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RecipeHerbMixture extends RecipeBase.Dynamic{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean milk_bottle = false;
		boolean ppotato = false;
		int powders = 0;
		
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem() instanceof IMixtureIngredient){
					powders++;
				}else if(stack.getItem() == MinaItems.MILK_BOTTLE &! milk_bottle){
					milk_bottle = true;
				}else if(stack.getItem() == Items.POISONOUS_POTATO &! ppotato){
					ppotato = true;
				}else{
					return false;
				}
			}
		}
		
		return milk_bottle && powders > 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		NonNullList<ItemStack> in = NonNullList.create();
		boolean ppotato = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem() instanceof IMixtureIngredient){
					in.add(stack);
				}else if(stack.getItem() == Items.POISONOUS_POTATO &! ppotato){
					ppotato = true;
				}else if(stack.getItem() != MinaItems.MILK_BOTTLE){
					return ItemStack.EMPTY;
				}
			}
		}
		final int l = in.size();
		int red = 0, green = 0, blue = 0;
		List<IMixtureApplicator> appls = new ArrayList<IMixtureApplicator>();
		for(int i = 0 ; i < l ; i++){
			ItemStack is = in.get(i);
			IMixtureIngredient ing = (IMixtureIngredient) is.getItem();
			int tint = ing.getIngredientColor(is);
			red += MinaUtils.extractRedFromColor(tint) / l;
			green += MinaUtils.extractGreenFromColor(tint) / l;
			blue += MinaUtils.extractBlueFromColor(tint) / l;
			ing.getMixtureProperties(is, appls);
		}
		ItemStack r = new ItemStack(MinaItems.MIXTURE);
		ItemHerbMixture.HerbMixtureSnack cap = (HerbMixtureSnack) r.getCapability(ISnack.CAPABILITY, null);
		cap.setColor(MinaUtils.convertRGBToDecimal(red, green, blue));
		
		final HashMap<StatType, Integer> pointsMap = new HashMap<>();
		for(IMixtureApplicator appl : appls){
			for(StatType type : appl.getSupportedStats()){
				pointsMap.merge(type, appl.getStatEffect(type), (a, b) -> MathHelper.clamp(a + b, -255, 255));//TODO: Dynamic bounds
			}
		}
		
		if(ppotato){
			int moy = pointsMap.values().stream().mapToInt(v -> v).sum();
			moy /= pointsMap.size();
			final int fmoy = moy;
			pointsMap.entrySet().forEach(e -> {
				int x = e.getValue() - fmoy;
				int y = (e.getValue() < 0)?-(x*x):(x*x);
				pointsMap.put(e.getKey(), y);
			});
		}

		pointsMap.entrySet().forEach(e -> {
			cap.offer(e.getKey(), e.getValue());
		});
		
		return r;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.MIXTURE);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {//leave it like that please
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) >= 2;
	}

}
