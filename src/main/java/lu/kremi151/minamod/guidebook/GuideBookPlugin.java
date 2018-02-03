package lu.kremi151.minamod.guidebook;

import com.creysys.guideBook.api.RecipeManager;

public class GuideBookPlugin {

	public static void register() {
		RecipeManager.registerHandler(new GuideBookCraftingRecipes());
		RecipeManager.registerHandler(new GuideBookCombinerRecipes());
	}
	
}
