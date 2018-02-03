package lu.kremi151.minamod.guidebook;

import java.util.ArrayList;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.RecipeHandler;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.recipe.combiner.CombinerRecipeManager;
import lu.kremi151.minamod.recipe.combiner.ICombinerRecipe;

public class GuideBookCombinerRecipes extends RecipeHandler{

	@Override
	public String getName() {
		return "guidebook.minamod.combining";
	}

	@Override
	public ArrayList<DrawableRecipe> getRecipes() {
		ArrayList<DrawableRecipe> res = new ArrayList<>(CombinerRecipeManager.getRecipes().size());
		for(ICombinerRecipe recipe : CombinerRecipeManager.getRecipes()) {
			try {
				DrawableCombinerRecipe r = DrawableCombinerRecipe.parse(recipe);
				if(r != null) {
					res.add(r);
				}
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	@Override
	public Object getTabIcon() {
		return MinaItems.COMBINER;
	}

	@Override
	public int recipesPerPage() {
		return 2;
	}

}
