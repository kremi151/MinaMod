package lu.kremi151.minamod.recipe;

import java.util.ArrayList;
import java.util.List;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.enums.EnumPlayerStat;
import lu.kremi151.minamod.interfaces.IMixtureApplicator;
import lu.kremi151.minamod.interfaces.IMixtureIngredient;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RecipeHerbMixture implements IRecipe{

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
		ItemStack r = new ItemStack(MinaItems.MIXTURE,1);
		NBTTagCompound nbt = r.getOrCreateSubCompound("mixture");
		nbt.setInteger("color", MinaUtils.convertRGBToDecimal(red, green, blue));
		
		//int atk = 0, def = 0, spd = 0;
		int stats[] = new int[EnumPlayerStat.values().length];
		for(IMixtureApplicator appl : appls){
			for(EnumPlayerStat stat : EnumPlayerStat.values()){
				stats[stat.ordinal()] = MathHelper.clamp(stats[stat.ordinal()] + appl.getStatEffect(stat), -255, 255);
			}
		}
		
		if(ppotato){
			int moy = 0;
			for(EnumPlayerStat stat : EnumPlayerStat.values())moy += stats[stat.ordinal()];
			moy /= EnumPlayerStat.values().length;
			for(EnumPlayerStat stat : EnumPlayerStat.values()){
				int x = stats[stat.ordinal()] - moy;
				int y = (stats[stat.ordinal()] < 0)?-(x*x):(x*x);
				stats[stat.ordinal()] = y;
			}
		}

		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			nbt.setInteger(stat.getId(), stats[stat.ordinal()]);
		}
		
		return r;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.MIXTURE,1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {//leave it like that please
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

}
