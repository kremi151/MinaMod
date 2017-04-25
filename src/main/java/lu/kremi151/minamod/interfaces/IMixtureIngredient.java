package lu.kremi151.minamod.interfaces;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IMixtureIngredient {

	int getIngredientColor(ItemStack stack);
	void getMixtureProperties(ItemStack stack, List<IMixtureApplicator> list);
	
}
